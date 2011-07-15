/* 
 * Copyright (c) 2002 Shaven Puppy Ltd
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are 
 * met:
 * 
 * * Redistributions of source code must retain the above copyright 
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'Shaven Puppy' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package it.marteEngine.test.fuzzy;

/**
 * Bresenham's famous line drawing algorithm. Works for 2D.
 */
public final class Bresenham {

	/** The start and end of the line */
	private int x1, y1, x2, y2;

	/** Used for calculation */
	private int dx, dy, error, x_inc, y_inc, xx, yy, length, count;

	/** General case algorithm */
	private static final Bresenham bresenham = new Bresenham();

	/**
	 * Construct a Bresenham algorithm.
	 */
	public Bresenham() {
	}

	/**
	 * Plot a line between (x1,y1) and (x2,y2). To step through the line use
	 * next().
	 * 
	 * @return the length of the line (which will be 1 more than you are
	 *         expecting).
	 */
	public int plot(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;

		// compute horizontal and vertical deltas
		dx = x2 - x1;
		dy = y2 - y1;

		// test which direction the line is going in i.e. slope angle
		if (dx >= 0) {
			x_inc = 1;
		} else {
			x_inc = -1;
			dx = -dx; // need absolute value
		}

		// test y component of slope

		if (dy >= 0) {
			y_inc = 1;
		} else {
			y_inc = -1;
			dy = -dy; // need absolute value
		}

		xx = x1;
		yy = y1;

		if (dx > 0)
			error = dx >> 1;
		else
			error = dy >> 1;

		count = 0;
		length = Math.max(dx, dy) + 1;
		return length;
	}

	/**
	 * Get the next point in the line. You must not call next() if the previous
	 * invocation of next() returned false.
	 * 
	 * Retrieve the X and Y coordinates of the line with getX() and getY().
	 * 
	 * @return true if there is another point to come.
	 */
	public boolean next() {
		// now based on which delta is greater we can draw the line
		if (dx > dy) {
			// adjust the error term
			error += dy;

			// test if error has overflowed
			if (error >= dx) {
				error -= dx;

				// move to next line
				yy += y_inc;
			}

			// move to the next pixel
			xx += x_inc;
		} else {
			// adjust the error term
			error += dx;

			// test if error overflowed
			if (error >= dy) {
				error -= dy;

				// move to next line
				xx += x_inc;
			}

			// move to the next pixel
			yy += y_inc;
		}

		count++;
		return count < length;
	}

	/**
	 * @return the current X coordinate
	 */
	public int getX() {
		return xx;
	}

	/**
	 * @return the current Y coordinate
	 */
	public int getY() {
		return yy;
	}

	/**
	 * Plot a line between (x1,y1) and (x2,y2). The results are placed in x[]
	 * and y[], which must be large enough.
	 * 
	 * @return the length of the line or the length of x[]/y[], whichever is
	 *         smaller
	 */
	public static final int plot(final int x1, final int y1, final int x2,
			final int y2, final int x[], final int y[]) {

		int length = Math.min(x.length,
				Math.min(y.length, bresenham.plot(x1, y1, x2, y2)));
		for (int i = 0; i < length; i++) {
			bresenham.next();
			x[i] = bresenham.getX();
			y[i] = bresenham.getY();
		}

		return length;

		/*
		 * 
		 * int dx; // difference in x's int dy; // difference in y's int error =
		 * 0; // the discriminant i.e. error i.e. decision variable int x_inc;
		 * int y_inc; int index; // used for looping
		 * 
		 * // compute horizontal and vertical deltas dx = x2 - x1; dy = y2 - y1;
		 * 
		 * // test which direction the line is going in i.e. slope angle if (dx
		 * >= 0) { x_inc = 1; } else { x_inc = -1; dx = -dx; // need absolute
		 * value
		 * 
		 * }
		 * 
		 * // test y component of slope
		 * 
		 * if (dy >= 0) { y_inc = 1; } else { y_inc = -1; dy = -dy; // need
		 * absolute value
		 * 
		 * }
		 * 
		 * int xx = x1, yy = y1;
		 * 
		 * // now based on which delta is greater we can draw the line if (dx >
		 * dy) { error = dx >> 1; // draw the line for (index = 0; index <= dx
		 * && index < x.length; index++) { // remember the point x[index] = xx;
		 * y[index] = yy;
		 * 
		 * // adjust the error term error += dy;
		 * 
		 * // test if error has overflowed if (error >= dx) { error -= dx;
		 * 
		 * // move to next line yy += y_inc; }
		 * 
		 * // move to the next pixel xx += x_inc; } return Math.min(x.length,
		 * dx); } else { error = dy >> 1; // draw the line for (index = 0; index
		 * <= dy && index < y.length; index++) { // remember the point x[index]
		 * = xx; y[index] = yy;
		 * 
		 * // adjust the error term error += dx;
		 * 
		 * // test if error overflowed if (error >= dy) { error -= dy;
		 * 
		 * // move to next line xx += x_inc; }
		 * 
		 * // move to the next pixel yy += y_inc; } return Math.min(y.length,
		 * dy); }
		 */
	}
}