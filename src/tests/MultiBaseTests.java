package tests;

import static org.junit.Assert.*;

import main.MultiBaseNum;

import org.junit.Test;

public class MultiBaseTests {

	private static int[] base = new int[] {4,3,2};
	
	@Test
	public void testSetIntCarry() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 2;
		int[] expected = new int[] {0,1,0};
		num.set(input);
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testSetIntTypical() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 1;
		int[] expected = new int[] {0,0,1};
		num.set(input);
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testSetIntMax() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 23;
		int[] expected = new int[] {3,2,1};
		num.set(input);
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testMul() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 2;
		int[] expected = new int[] {0,2,0};
		num.set(input);
		num.mul(2);
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testDiv() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 2;
		int[] expected = new int[] {0,0,1};
		num.set(input);
		num.div(2);
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testDivRound() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 7;
		int[] expected = new int[] {0,1,1};
		num.set(input);
		num.div(2);
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testInc() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 7;
		int[] expected = new int[] {1,1,0};
		num.set(input);
		num.inc();
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testDec() {
		MultiBaseNum num = new MultiBaseNum(base);
		int input = 7;
		int[] expected = new int[] {1,0,0};
		num.set(input);
		num.dec();
		int[] actual = num.getValue();
		assertArrayEquals(expected, actual);
	}

}
