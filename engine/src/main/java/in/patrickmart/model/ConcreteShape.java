package in.patrickmart.model;

import java.util.ArrayList;
import java.util.List;

/**
 *	@author Patrick Martin
 *	@version 0.1
 *  Uses a set of Vector2Ds to define a convex shape.
 */
public class ConcreteShape implements Shape {
    private List<Vector2D> points;  //A collection of vectors representing offsets from the shape's center of gravity.
    private Vector2D position;
    private double rotation;
	private double area; //The area of this shape, used for calculations of mass.

    /**
     * Constructor for objects of Class Shape
     * @param points an already-constructed list of Vector2D objects defining the points that make up this shape.
     */
    public ConcreteShape(List<Vector2D> points) {
		//Construct this model from a set of vectors or x/y pairs.
        this.points = points;

        this.rotation = 0; // No rotation provided, default is zero.
		
        //Correct the center of gravity.
        calculateCenterOfGravity();
		
		//Calculate the area of this planar shape.
        calculateArea();
    }

    /**
     * Constructor for objects of Class Shape, constructs an n-sided polygon.
     * @param n number of vertices to construct this model from.
     * @param radius how far each vertex is from the center of gravity.
     */
    public ConcreteShape(int n, double radius) {
        //Calculate the angle (in radians) between each vector and the next.
        // The first vertex is directly above the center of gravity.
        double rotation = Math.PI * 2 / n;
		//Construct this model from a set of calculated vectors.
		this.points = new ArrayList<Vector2D>();
        for (int i = 0; i < n; i++) {
            this.points.add(new Vector2D(0, radius).rotate(rotation * i));
        }

        this.rotation = 0; // No rotation provided, default is zero.

        //Correct the center of gravity.
        calculateCenterOfGravity();
		
		//Calculate the area of this planar shape.
        calculateArea();
    }

    /**
     * Default Constructor for objects of Class Shape, constructs a triangle with a 1m "radius".
     */
    public ConcreteShape() {
        int n = 3;
        int radius = 1;

        //Calculate the angle (in radians) between each vector and the next.
        // The first vertex is directly above the center of gravity.
        this.points = new ArrayList<Vector2D>();

        //Add each vertex and rotate it into position.
        double rotation = Math.PI * 2 / n; // How much should each
        for (int i = 0; i < n; i++) {
            this.points.add(new Vector2D(0, radius).rotate(rotation * i));
        }

        this.rotation = 0; // No rotation provided, default is zero.
        
        //Correct the center of gravity.
        calculateCenterOfGravity();
        //Construct this model from a set of vectors or x/y pairs.
        calculateArea();
    }


    /**
     *  Adjust all vectors in this model so that their origin is the model's center of gravity.
     */
    private void calculateCenterOfGravity() {
        // Calculate the offset vector from the origin to the centroid (centroid = (x1 + x2 + ... + xk) / k)
        double x = 0;
        double y = 0;
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
        //If there are less than 3 points, you have a line or dot instead of a shape.
        if (points.size() < 3) {
            return;
        }
        //Calculate the area of each triangle making up this shape.
        for(int i = 0; i < points.size(); i++) {
            Vector2D first = points.get(i);
            Vector2D second = points.get((i + 1) % points.size());
            this.area += Math.abs(first.getX() * second.getY() - first.getY() * second.getX()) / 2;
        }
    }
	
	/**
	 * Calculate the bounding box of this model at its current rotation.
	 */
	public AABB calculateBounds() {
		double furthestX = 0;
		double furthestY = 0;
		
		for (Vector2D p : points) {
			if (Math.abs(p.getX()) > furthestX) {
				furthestX = Math.abs(p.getX());
			}
			if (Math.abs(p.getY()) > furthestY) {
				furthestY = Math.abs(p.getY());
			}
		}
		
		return new AABB(new Vector2D(), furthestX, furthestY);
	}

    /**
     * Unimplemented, Use AABB containsPoint instead. Determines if a point is within the model.
     * @param point
     * @return true if point is within model.
     */
    public boolean containsPoint(Vector2D point) {
        return false;
    }

    /**
     * Implements Hyperplane Separation Theorem, the best named theorem in existence, to determine intersection.
     * @param other The model to check collision against
     * @return true if this model and the other model are intersecting
     */
    public Vector2D intersectsShape(Shape other) {
        // Build a list of normal vectors from both shapes. Each normal is one of our axes
        ArrayList<Vector2D> axes = getNormals();
        axes.addAll(other.getNormals());

        double minOverlap = Double.MAX_VALUE; //Biggest possible double.
        Vector2D minVector = null;
        // For each axis, find the min and max dot product of that axis with each point in this shape and the other
        for (Vector2D axis : axes) {
            double[] projection = project(axis);
            double min = projection[0];
            double max = projection[1];

            double[] otherProjection = other.project(axis);
            double oMin = otherProjection[0];
            double oMax = otherProjection[1];

            // Determine if there is any overlap between the min/max of this and the other shape. if not, return false
            // seems to say there is no collision only when the objects have space between them on the x axis.
            if (!(min <= oMax && oMin <= max)) {
                return null;
            } else {
                // If there is any overlap, find out how much. Keep track of the minimum so we can return it in the mtv.
                // overlap = maximum(0, minimum(oMax, max) - maximum(oMin, min))
                double overlap = Math.max(0, Math.min(max, oMax) - Math.max(min, oMin));
                // Determine if we have the mtv backwards.
                if (overlap < minOverlap) {
                    minOverlap = overlap;
                    if (min < oMin) {
                        minVector = axis.copy().setMag(overlap); // The minimum translation vector.
                    } else {
                        minVector = axis.copy().mult(-1).setMag(overlap); // The corrected minimum translation vector.
                    }
                }
            }
        }
        return minVector;
    }

    /**
     * Projects this model onto an axis, and returns the interval of that projection.
     * @param axis A normal vector to project this model onto.
     * @return The minimum and maximum dot product of the points in this model.
     */
    public double[] project(Vector2D axis) {
        double min = points.get(0).copy().add(position).dot(axis); // Start the min and max on a calculated value. 0 might not be in range.
        double max = points.get(0).copy().add(position).dot(axis);

        for (int i = 1; i < points.size(); i++) { // Find the projection of this model on this axis
            double dot = points.get(i).copy().add(position).dot(axis);

            if (dot < min) {
                min = dot;
            }
            if (dot > max) {
                max = dot;
            }
        }

        return new double[] {min, max};
    }

    /**
     * Calculates the edge vectors that define this shape. An edge is a line between two points from the points list.
     * @return An ArrayList of every edge vector in this model.
     */
    private ArrayList<Vector2D> getEdges() {
        ArrayList<Vector2D> edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            // edge[n] = points[n + 1] - points[n];
            edges.add(points.get((i + 1) % points.size()).copy().sub(points.get(i)));
        }
        return edges;
    }

    /**
     * Calculates the normal vectors that define this shape. A normal vector is perpendicular to an edge vector.
     * @return An ArrayList of every normal vector in this model.
     */
    public ArrayList<Vector2D> getNormals() {
        ArrayList<Vector2D> normals = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            // edge[n] = points[n + 1] - points[n];
            // Get the normal vector of each edge vector by swapping x and y, then negating one of them.
            normals.add(points.get((i + 1) % points.size()).copy().sub(points.get(i)).getNormal());
        }
        return normals;
    }

    /**
     * setter for position
     * @param position
     */
    public void setPosition(Vector2D position) {
        this.position = position;
    }

    /**
     * getter for position.
     * @return shape position
     */
    public Vector2D getPosition() {
        if (position != null) {
            return position;
        }
        return new Vector2D();
    }

    /**
     * setter for rotation
     * @param rotation
     */
    public void setRotation(double rotation) {
        // Find the difference between the new rotation and the previous.
        double difference = rotation - this.rotation;

        // Rotate all points by the difference, update the stored rotation.
        rotate(difference);
    }

    /**
     * apply rotation.
     * @param rotation
     */
    public void rotate(double rotation) {
        // Update the stored rotation.
        this.rotation += rotation;

        // Rotate all of the points in this shape by the provided rotation.
        for (Vector2D point : points) {
            point.rotate(rotation);
        }
    }
    /**
     * getter for rotation
     * @return rotation
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * getter for points of a shape
     * @return list of points
     */
    public List<Vector2D> getPoints() {
        return this.points;
    }

    /**
     * getter for area.
     * @return area
     */
    public double getArea() {
        return this.area;
    }

    /**
     * getter for diameter.
     * @return diameter
     */
    public double getDiameter() {
        double sum = 0;
        for (Vector2D p : points) {
            sum += p.mag();
        }
        return (sum / points.size()) * 2;
    }

    /**
     * getter for subShapes
     * @return singletonSubShape
     */
    public ArrayList<Shape> getSubShapes(){
        ArrayList<Shape> singletonSubShape = new ArrayList<>();
        singletonSubShape.add(this);
        return singletonSubShape;
    }

    /**
     * will create a complex shape when implemented
     * @param shape
     */
    public void addShape(Shape shape) { }

    /**
     * getter for shape object.
     * @param index
     * @return shape object
     */
    public Shape getShape(int index) {
        return this;
    }

    /**
     * for composite shapes only.
     * @param shape
     */
    public void removeShape(Shape shape) { }
}
