package in.patrickmart.model;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Vector;

/**
 * Unit test for simple App.
 */
public class Vector2DTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Vector2DTest(String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( Vector2DTest.class );
    }

    /**
     * Test the creation of new vectors.
     */
    public void testConstructors()
    {
        Vector2D v = new Vector2D(); //Default constructor
        assertTrue( v.getX() == 0 && v.getY() == 0);
        v = new Vector2D(2, 3); //Custom X and Y constructor
        assertTrue( v.getX() == 2 && v.getY() == 3);
        Vector2D u = v.copy(); //Duplication of a vector.
        assertTrue(u.getX() == 2 && v.getY() == 3);

        u = new Vector2D().random(); //Random vector
        assertTrue(u.getX() >= -1 && u.getX() <= 1 && u.getY() >= -1 && u.getY() <= 1);
    }

    /**
     * tests the set and get methods of the library.
     */
    public void testGettersAndSetters()
    {
        Vector2D v = new Vector2D(5,5);
        double x = 5;
        double y = 5;
        assertEquals(x,v.getX());
        assertEquals(y,v.getY());
        x = 7;
        y = 9;
        v.setX(x);
        v.setY(y);
        assertEquals(x,v.getX());
        assertEquals(y,v.getY());
        v.set(x,y);
        assertEquals(x,v.getX());
        assertEquals(y,v.getY());
    }

    /**
     * tests the vector to string method.
     */
    public void testVectorToString()
    {
        double x = 9;
        double y = 8;
        Vector2D v = new Vector2D(x,y);
        assertEquals("(9.0, 8.0)",v.toString());
    }

    /**
     * tests that the copied vector matches the initial vector.
     */
    public void testVectorCopy()
    {
        double x = 8;
        double y = 9;
        Vector2D v = new Vector2D(x,y);
        Vector2D c = v.copy();
        assertEquals(v.getX(),c.getX());
        assertEquals(v.getY(),c.getY());
    }

    /**
     * tests vector subtraction.
     */
    public void testSubtract()
    {
        Vector2D v1 = new Vector2D(2,7);
        Vector2D v2 = new Vector2D(5,9);
        Vector2D result = new Vector2D(2 - 5, 7 - 9);
        assertEquals(result.toString(), v1.sub(v2).toString());
    }

    /**
     * tests vector addition.
     */
    public void testAddition()
    {
        Vector2D v1 = new Vector2D(2,7);
        Vector2D v2 = new Vector2D(5,9);
        Vector2D result = new Vector2D(2 + 5, 7 + 9);
        assertEquals(result.toString(), v1.add(v2).toString());
    }

    /**
     * tests vector multiplication.
     */
    public void testMultiplication()
    {
        double x = 4;
        double y = 6;
        double s = 5;
        Vector2D v = new Vector2D(x,y);
        Vector2D result = new Vector2D(x * s, y * s);
        assertEquals(result.toString(), v.mult(s).toString());
    }

    /**
     * tests vector division.
     */
    public void testDivision()
    {
        double x = 4;
        double y = 6;
        double s = 5;
        Vector2D v = new Vector2D(x,y);
        Vector2D result = new Vector2D(x / s, y / s);
        assertEquals(result.toString(), v.div(s).toString());
    }

    /**
     * tests vector magnitude.
     */
    public void testVectorMag()
    {
        double x = 0;
        double y = 4;
        Vector2D v = new Vector2D(x,y);
        assertEquals(4.0,v.mag());
    }

    /**
     * tests that vector magsqr returns the magnitude squared
     */
    public void testVectorMagSqr()
    {
        double x = 4;
        double y = 4;
        Vector2D v = new Vector2D(x,y);
        assertEquals(32.0,v.magSq());
    }

    /**
     * tests vector dot product.
     */
    public void testVectorDot()
    {
        Vector2D v1 = new Vector2D(2,7);
        Vector2D v2 = new Vector2D(5,9);
        double result = v1.getX() * v2.getX() + v1.getY() * v2.getY();
        assertEquals(result, v1.dot(v2));
    }

    /**
     * tests vector cross product.
     */
    public void testVectorCross()
    {
        Vector2D v1 = new Vector2D(2,7);
        Vector2D v2 = new Vector2D(5,9);
        double result = v1.getX() * v2.getY() - v1.getY() * v2.getX();
        assertEquals(result, v1.cross(v2));
    }

    /**
     * tests the distance calculation between vectors.
     */
    public void testVectorDist()
    {
        Vector2D v1 = new Vector2D(4,4);
        Vector2D v2 = new Vector2D(4,6);
        assertEquals(2.0, v1.dist(v2));
        v2.set(6,4);
        assertEquals(2.0, v2.dist(v1));
    }

    /**
     * tests normalization of a vector.
     */
    public void testVectorNormalize()
    {
        Vector2D v = new Vector2D(2.0,2.0);
        Vector2D result = v.copy();
        assertEquals(result.setMag(1).toString(), v.normalize().toString());

        //test zero vector normalized
        v.setX(0);
        v.setY(0);
        v.normalize();
        assertEquals(0.0,v.mag());
    }

    /**
     * tests vectors setMag.
     */
    public void testVectorSetMag()
    {
        Vector2D v = new Vector2D(4,6);
        v.setMag(4);
        assertEquals(4.0, v.mag());
    }

    /**
     * tests vector limit.
     */
    public void testVectorLimit()
    {
        Vector2D v = new Vector2D(4,6);
        Vector2D result = v.setMag(1);
        assertEquals(result.toString(), v.normalize().toString());
    }


    /**
     * test vector angle calculations.
     */
    public void testVectorAngleMath()
    {
        Vector2D v = new Vector2D(4,6);
        double result = Math.atan2(v.getY(), v.getX());
        assertEquals(result,v.heading());

        Vector2D v2 = new Vector2D(5,6);
        result = v.heading() - v2.heading();
        assertEquals(result, v2.angleBetween(v));

        Vector2D v3 = new Vector2D(0, 2);
        Vector2D v4 = new Vector2D(2, 0);

        assertTrue(v3.angle() == Math.PI/2);
        assertTrue(v4.angle() == 0);
        assertTrue(v3.angleBetween(v4) == -Math.PI/2);
        assertTrue(v4.angleBetween(v3) == Math.PI/2);

        Vector2D v5 = new Vector2D(0, -2);

        assertTrue(v3.angleBetween(v5) == Math.PI || v3.angleBetween(v5) == -Math.PI);


        v2.setX(Math.cos(90));
        v2.setY(Math.sin(90));
        assertEquals(v2.toString(),v.fromAngle(90).toString());
    }

    /**
     * test vector angle conversions
     */
    public void testVectorAngleConversions()
    {
        double test = 4.0;
        double result = test * 57.2957795131;
        assertEquals(result, Vector2D.radiansToDegrees(test));

        result = test / 6.28318530718;
        assertEquals(result, Vector2D.radiansToRevolutions(test));

        result = test * 360;
        assertEquals(result, Vector2D.revolutionsToDegrees(test));

        result = test * 6.28318530718;
        assertEquals(result, Vector2D.revolutionsToRadians(test));

        result = test * 0.01745329251;
        assertEquals(result, Vector2D.degreesToRadians(test));

        result = test / 360;
        assertEquals(result, Vector2D.degreesToRevolutions(test));
    }

    /**
    * tests vector rotation.
    */
    public void testVectorRotate()
    {
        Vector2D v = new Vector2D(4,6);
        double angle = Vector2D.degreesToRadians(90);
        double rot = v.heading() - angle;
        double mag = v.mag();
        Vector2D result = new Vector2D(Math.cos(rot) * mag,Math.sin(rot) * mag);
        assertEquals(result.toString(), v.rotate(angle).toString());
    }

    /**
     * Tests whether perpendicular vectors are good.
     */
    public void testVectorPerpendiculars() {
        Vector2D v = new Vector2D(2, -1);
        Vector2D normal = v.getPerpendicular();

        assertTrue(normal.getX() == -1);
        assertTrue(normal.getY() == -2);
    }

    /**
     * Tests whether normal vectors are good.
     */
    public void testVectorNormals() {
        Vector2D v = new Vector2D(2, -1);
        Vector2D normal = v.getNormal();
        double acceptableError = 0.0000000000001;
        assertTrue(normal.mag() >= 1 - acceptableError
                && normal.mag() <= 1 + acceptableError);
        assertTrue(normal.getX() >= -0.4472135954999579 - acceptableError
                && normal.getX() <= -0.4472135954999579 + acceptableError);
        assertTrue(normal.getY() >= -0.8944271909999159 - acceptableError
                && normal.getY() <= -0.8944271909999159 + acceptableError);
    }

    /**
     * test vector comparisons.
     */
    public void testVectorComparisons()
    {
        Vector2D v = new Vector2D(4,6);
        double[] result = {4,6};
        assertEquals(result[0], v.array()[0]);
        assertEquals(result[1], v.array()[1]);

        Vector2D v2 = v.copy();
        assertTrue(v.equals(v2));
    }
}
