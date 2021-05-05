package edu.nmsu.cs.circles;

/***
 * Example JUnit testing class for Circle1 (and Circle)
 *
 * - must have your classpath set to include the JUnit jarfiles - to run the test do: java
 * org.junit.runner.JUnitCore Circle1Test - note that the commented out main is another way to run
 * tests - note that normally you would not have print statements in a JUnit testing class; they are
 * here just so you see what is happening. You should not have them in your test cases.
 ***/

import org.junit.*;

public class Circle1Test
{
	// Data you need for each test case
	private Circle1 circle1;

	//
	// Stuff you want to do before each test case
	//
	@Before
	public void setup()
	{
		System.out.println("\nTest starting...");
		circle1 = new Circle1(1, 2, 3);
	}

	//
	// Stuff you want to do after each test case
	//
	@After
	public void teardown()
	{
		System.out.println("\nTest finished.");
	}

	//
	// Test circle initialization with positive values
	//
	@Test
	public void circleInitTestPos()
	{
		Circle1 testCircle;
		System.out.println("Running test circleInitTestPos.");
		testCircle = new Circle1(1, 5, 3);
		Assert.assertTrue(testCircle.center.x == 1 &&
						  testCircle.center.y == 5 &&
						  testCircle.radius == 3);
	}
	
	//
	// Test circle initialization with negative values
	// Radius should still be a positive value because a circle can't have a negative radius
	//
	@Test
	public void circleInitTestNeg()
	{
		Circle1 testCircle;
		System.out.println("Running test circleInitTestNeg.");
		testCircle = new Circle1(-4, -7, -2);
		Assert.assertTrue(testCircle.center.x == -4 &&
						  testCircle.center.y == -7 &&
						  testCircle.radius == 2);
	}
	
	//
	// Test a simple positive move
	//
	@Test
	public void simpleMove()
	{
		Point p;
		System.out.println("Running test simpleMove.");
		p = circle1.moveBy(1, 1);
		Assert.assertTrue(p.x == 2 && p.y == 3);
	}

	//
	// Test a simple negative move
	//
	@Test
	public void simpleMoveNeg()
	{
		Point p;
		System.out.println("Running test simpleMoveNeg.");
		p = circle1.moveBy(-1, -1);
		Assert.assertTrue(p.x == 0 && p.y == 1);
	}

	//
	// Test an increase of the radius
	//
	@Test
	public void radiusIncrease()
	{
		double r;
		System.out.println("Running test radiusIncrease.");
		r = circle1.scale(2);
		Assert.assertTrue(r == 6);
	}
	
	//
	// Test a decrease of the radius
	//
	@Test
	public void radiusDecrease()
	{
		double r;
		System.out.println("Running test radiusDecrease.");
		r = circle1.scale(0.5);
		Assert.assertTrue(r == 1.5);
	}
	
	//
	// Check that intersects returns true when there is an intersect
	// Testing only on changing x values.
	//
	@Test
	public void intersectsXShift()
	{
		System.out.println("Running test intersectsXShift.");
		Circle1 testCircle;
		testCircle = new Circle1(8, 2, 5);
		Assert.assertTrue(circle1.intersects(testCircle));
	}
	
	//
	// Check that intersects returns true when there is an intersect
	// Testing only on changing y values.
	//
	@Test
	public void intersectsYShift()
	{
		System.out.println("Running test intersectsYShift.");
		Circle1 testCircle;
		testCircle = new Circle1(1, 6, 5);
		Assert.assertTrue(circle1.intersects(testCircle));
	}
	
	//
	// Check that intersects returns true when there is an intersect
	// Testing on changing x and y values.
	//
	@Test
	public void intersectsXY()
	{
		System.out.println("Running test intersectsXY.");
		Circle1 testCircle;
		testCircle = new Circle1(7, 6, 5);
		Assert.assertTrue(circle1.intersects(testCircle));
	}
	
	//
	// Check that intersects returns false when there is no intersect
	//
	@Test
	public void intersectsFalse()
	{
		System.out.println("Running test intersectsFalse.");
		Circle1 testCircle;
		testCircle = new Circle1(9, 7, 5);
		Assert.assertTrue(!circle1.intersects(testCircle));
	}
}
