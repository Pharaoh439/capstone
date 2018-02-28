package in.patrickmart.model;

import java.lang.reflect.Array;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CompositeModel2D {
    private ArrayList<Model2D> models;
    private Vector2D position;
    private double area;

    public CompositeModel2D(ArrayList<Model2D> models){
        this.models = models;
        calculateArea();
        calculateCenterOfGravity();
    }

    public void addModel(Model2D model){
        models.add(model);
    }

    public Model2D getModel(int index){
        return models.get(index);
    }

    public void removeModel(Model2D model){
        models.remove(model);
    }

    /**
     *  Adjust all vectors in this model so that their origin is the model's center of gravity.
     */
    private void calculateCenterOfGravity() {
        // Calculate the offset vector from the origin to the centroid (centroid = (x1 + x2 + ... + xk) / k)
        double x = 0;
        double y = 0;
        ArrayList<Vector2D> points = new ArrayList<>();
        for(Model2D m: models){
            List<Vector2D> subpoints = m.getPoints();
            for (Vector2D point : subpoints) {
                if (!points.contains(point)) {
                    points.add(point);
                }
            }
        }
        for (Vector2D p : points) {
            x += p.getX();
            y += p.getY();
        }
        x /= points.size();
        y /= points.size();
        Vector2D offset = new Vector2D(x, y);
        // Subtract the offset vector from every point, effectively moving the origin to the center of gravity.
        for (Vector2D p : points) {
            p.sub(offset);
        }
    }

    /**
     * Calculate the area of this model by calculating triangular area between the center and every 2 adjacent points.
     * @return the area of this Model.
     */
    private void calculateArea() {
        this.area = 0;
        for(Model2D m: models){
            this.area += m.getArea();
        }
    }

    /**
     * Calculate the bounding box of this model at its current rotation.
     */
    public AABB calculateBounds(double rotation) {
        ArrayList<Vector2D> points = new ArrayList<>();
        for(Model2D m: models){
            List<Vector2D> subpoints = m.getPoints();
            for (Vector2D point : subpoints) {
                points.add(point);
            }
        }
        double furthestX = 0;
        double furthestY = 0;

        for (Vector2D p : points) {
            Vector2D q = p.copy().rotate(rotation);
            if (Math.abs(q.getX()) > furthestX) {
                furthestX = Math.abs(q.getX());
            }
            if (Math.abs(q.getY()) > furthestY) {
                furthestY = Math.abs(q.getY());
            }
        }

        return new AABB(new Vector2D(), furthestX, furthestY);
    }

    /**
     * determines if a point is within the model
     * @param point
     * @return true if point is within model.
     */
    public boolean containsPoint(Vector2D point) {
        for(Model2D m: models){
            if(m.containsPoint(point)){
                return true;
            }
        }
        return false;
    }

    /**
     * Implements Hyperplane Separation Theorem, the best named theorem in existence, to determine intersection.
     * @param other The model to check collision against
     * @return true if this model and the other model are intersecting
     */
    public boolean intersectsModel2D(in.patrickmart.model.Model2D other) {
        //TODO: change this so that it returns collisionData
        for(Model2D m: models){
            if(m.intersectsModel2D(other)){
                return true;
            }
        }
        return false;
    }


    public void setPosition(Vector2D position) {
        this.position = position;
    }
    public Vector2D getPosition() {
        if (position != null) {
            return position;
        }
        return new Vector2D();
    }
    public List<Vector2D> getPoints() {
        List<Vector2D> points = new ArrayList<>();
        for(Model2D m: models){
            List<Vector2D> subpoints = m.getPoints();
            for (Vector2D point : subpoints) {
                if (!points.contains(point)) {
                    points.add(point);
                }
            }
        }
        return points;
    }

    public ArrayList<Model2D> getSubModels(){
        ArrayList<Model2D> subModels= new ArrayList<>();
        for(Model2D m: models){
            subModels.addAll(m.getSubModels());
        }
        return subModels;
    }
    public double getArea(){
        return this.area;
    }
}
