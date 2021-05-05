package edu.nmsu.cs.circles;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Circle2Test {
	// Data you need for each test case
		private Circle2 circle2;

		//
		// Stuff you want to do before each test case
		//
		@Before
		public void setup()
		{
			System.out.println("\nTest starting...");
			circle2 = new Circle2(1, 2, 3);
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
			Circle2 testCircle;
			System.out.println("Running test circleInitTestPos.");
			testCircle = new Circle2(1, 5, 3);
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
			Circle2 testCircle;
			System.out.println("Running test circleInitTestNeg.");
			testCircle = new Circle2(-4, -7, -2);
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
			p = circle2.moveBy(1, 1);
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
			p = circle2.moveBy(-1, -1);
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
			r = circle2.scale(2);
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
			r = circle2.scale(0.5);
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
			Circle2 testCircle;
			testCircle = new Circle2(8, 2, 5);
			Assert.assertTrue(circle2.intersects(testCircle));
		}
		
		//
		// Check that intersects returns true when there is an intersect
		// Testing only on changing y values.
		//
		@Test
		public void intersectsYShift()
		{
			System.out.println("Running test intersectsYShift.");
			Circle2 testCircle;
			testCircle = new Circle2(1, 6, 5);
			Assert.assertTrue(circle2.intersects(testCircle));
		}
		
		//
		// Check that intersects returns true when there is an intersect
		// Testing on changing x and y values.
		//
		@Test
		public void intersectsXY()
		{
			System.out.println("Running test intersectsXY.");
			Circle2 testCircle;
			testCircle = new Circle2(7, 6, 5);
			Assert.assertTrue(circle2.intersects(testCircle));
		}
		
		//
		// Check that intersects returns false when there is no intersect
		//
		@Test
		public void intersectsFalse()
		{
			System.out.println("Running test intersectsFalse.");
			Circle2 testCircle;
			testCircle = new Circle2(9, 7, 5);
			Assert.assertTrue(!circle2.intersects(testCircle));
		}
}
