/**
 * Introduction to Computer Science 2026, Ariel University,
 * Ex1: arrays, static functions and JUnit
 * https://docs.google.com/document/d/1GcNQht9rsVVSt153Y8pFPqXJVju56CY4/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 *
 * This class represents a set of static methods on a polynomial functions - represented as an array of doubles.
 * The array {0.1, 0, -3, 0.2} represents the following polynomial function: 0.2x^3-3x^2+0.1
 * This is the main Class you should implement (see "add your code below")
 *
 * @author boaz.benmoshe

 */
public class Ex1 {
	/** Epsilon value for numerical computation, it serves as a "close enough" threshold. */
	public static final double EPS = 0.001; // the epsilon to be used for the root approximation.
	/** The zero polynomial function is represented as an array with a single (0) entry. */
	public static final double[] ZERO = {0};
	/**
	 * Computes the f(x) value of the polynomial function at x.
	 * @param poly - polynomial function
	 * @param x
	 * @return f(x) - the polynomial function value at x.
	 */
	public static double f(double[] poly, double x) {
		double ans = 0;
		for(int i=0;i<poly.length;i++) {
			double c = Math.pow(x, i);
			ans += c*poly[i];
		}
		return ans;
	}
	/** Given a polynomial function (p), a range [x1,x2] and an epsilon eps.
	 * This function computes an x value (x1<=x<=x2) for which |p(x)| < eps, 
	 * assuming p(x1)*p(x2) <= 0.
	 * This function should be implemented recursively.
	 * @param p - the polynomial function
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
	 * @return an x value (x1<=x<=x2) for which |p(x)| < eps.
	 */
    public static double root_rec(double[] p, double x1, double x2, double eps) {
        double f1 = f(p,x1);
        double x12 = (x1+x2)/2;
        double f12 = f(p,x12);
        if (Math.abs(f12)<eps) {return x12;}
        if(f12*f1<=0) {return root_rec(p, x1, x12, eps);}
        else {return root_rec(p, x12, x2, eps);}
    }
	/**
	 * This function computes a polynomial representation from a set of 2D points on the polynom.
	 * The solution is based on: //	http://stackoverflow.com/questions/717762/how-to-calculate-the-vertex-of-a-parabola-given-three-points
	 * Note: this function only works for a set of points containing up to 3 points, else returns null.
	 * @param xx
	 * @param yy
	 * @return an array of doubles representing the coefficients of the polynom.
	 */

    //get two points (double)
	public static double[] PolynomFromPoints(double[] xx, double[] yy) {
		double [] ans = null;
		int lx = xx.length;
		int ly = yy.length;
		if(xx!=null && yy!=null && lx==ly && lx>1 && lx<4) {
            if(lx == 2) {
                double x1 = xx[0], y1 = yy[0];
                double x2 = xx[1], y2 = yy[1];
                double slope= (y2-y1) / (x2-x1); //shipua
                double b = y1 - slope*x1; //hituh
                ans = new double[]{b, slope};
            }
            if(lx == 3) {
                double x1 = xx[0], y1 = yy[0]; //leftest
                double x2 = xx[1], y2 = yy[1]; //middle
                double x3 = xx[2], y3 = yy[2]; //rightest
                double slope1 = (y2 - y1) / (x2 - x1); //shipua 1
                double slope2 = (y3 - y2) / (x3 - x2); //shipua 2
                double a2 = (slope2 - slope1) / (x3 - x1);// mekadem x^2
                double y1New = y1 - a2 * (x1 * x1); //revised
                double y2New = y2 - a2 * (x2 * x2); //revised
                double a1 = (y2New - y1New) / (x2 - x1);
                double a0 = y1New - (a1 * x1);

                return new double[] {a0, a1, a2};
            }
		}
		return ans;
	}
	/** Two polynomials functions are equal if and only if they have the same values f(x) for n+1 values of x,
	 * where n is the max degree (over p1, p2) - up to an epsilon (aka EPS) value.
	 * @param p1 first polynomial function
	 * @param p2 second polynomial function
	 * @return true iff p1 represents the same polynomial function as p2.
	 */
	public static boolean equals(double[] p1, double[] p2) {

        int maxPoints = Math.max(p1.length, p2.length);
        //loop
        for (int i = 0; i < maxPoints; i++) {
            double x = i;
            //use the f function
            double val1 = f(p1, x);
            double val2 = f(p2, x);

            //use abs to check the paar between the two
            //if the paar is < EPS return false
            if (Math.abs(val1 - val2) > EPS) {
                return false;
            }
        }
        boolean ans = true;
		return ans;
	}

	/** 
	 * Computes a String representing the polynomial function.
	 * For example the array {2,0,3.1,-1.2} will be presented as the following String  "-1.2x^3 +3.1x^2 +2.0"
	 * @param poly the polynomial function represented as an array of doubles
	 * @return String representing the polynomial function:
	 */
	public static String poly(double[] poly) {
		String ans = "";
		if(poly.length==0) {ans="0";}
        for (int i = poly.length -1; i >= 0 ; i--) {
            double val = poly[i];
            if (val == 0) {
                continue;
            }
            if (val > 0 && !ans.isEmpty()) {
                ans += "+";
            }
            ans += val;

            if (i == 1) {
                ans += "x";
            }else if (i > 1) {
                ans += "x^" + i;
            }
        }
        //check if no string was provided
        if(ans.length() == 0) {
            return "0";
        }

		return ans;
	}
	/**
	 * Given two polynomial functions (p1,p2) - "f" "g", a range [x1,x2] and an epsilon eps. This function computes an x value (x1<=x<=x2)
	 * for which |p1(x) -p2(x)| < eps, assuming (p1(x1)-p2(x1)) * (p1(x2)-p2(x2)) <= 0.
	 * @param p1 - first polynomial function
	 * @param p2 - second polynomial function
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param eps - epsilon (positive small value (often 10^-3, or 10^-6).
	 * @return an x value (x1<=x<=x2) for which |p1(x) - p2(x)| < eps.
	 */
	public static double sameValue(double[] p1, double[] p2, double x1, double x2, double eps) {

        int len = Math.max(p1.length, p2.length);
        double[] diff = new double[len];

        for (int i = 0; i < len; i++) {
            double v1 = 0;
            double v2 = 0;

            if (i < p1.length) {
                v1 = p1[i];
            }
            if (i < p2.length) {
                v2 = p2[i];
            }
            diff[i] = v1 - v2;
        }
        return root_rec(diff, x1, x2, eps);

	}
	/**
	 * Given a polynomial function (p), a range [x1,x2] and an integer with the number (n) of sample points.
	 * This function computes an approximation of the length of the function between f(x1) and f(x2) 
	 * using n inner sample points and computing the segment-path between them.
	 * assuming x1 < x2. 
	 * This function should be implemented iteratively (none recursive).
	 * @param p - the polynomial function
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param numberOfSegments - (A positive integer value (1,2,...).
	 * @return the length approximation of the function between f(x1) and f(x2).
	 */
	public static double length(double[] p, double x1, double x2, int numberOfSegments) {
        double dt = (x2 -x1) / numberOfSegments;
        double sum = 0;
        double xCurrent = x1;
        double yCurrent = f(p, x1);

        //loop as per the numbers of segments
        for (int i = 0; i < numberOfSegments; i++) {
            double xNext = xCurrent + dt;
            double yNext = f(p, xNext);
            double pow1 = Math.pow((xNext - xCurrent), 2);
            double pow2 = Math.pow((yNext - yCurrent), 2);
            double distance = Math.sqrt((pow1 + pow2));
            sum += distance;
            //update the values
            xCurrent = xNext;
            yCurrent = yNext;
        }
        return sum;
	}
	
	/**
	 * Given two polynomial functions (p1,p2), a range [x1,x2] and an integer representing the number of Trapezoids between the functions (number of samples in on each polynom).
	 * This function computes an approximation of the area between the polynomial functions within the x-range.
	 * The area is computed using Riemann's like integral (https://en.wikipedia.org/wiki/Riemann_integral)
	 * @param p1 - first polynomial function
	 * @param p2 - second polynomial function
	 * @param x1 - minimal value of the range
	 * @param x2 - maximal value of the range
	 * @param numberOfTrapezoid - a natural number representing the number of Trapezoids between x1 and x2.
	 * @return the approximated area between the two polynomial functions within the [x1,x2] range.
	 */
	public static double area(double[] p1,double[]p2, double x1, double x2, int numberOfTrapezoid) {
		double ans = 0;
        /** add you code below

         /////////////////// */
		return ans;
	}
	/**
	 * This function computes the array representation of a polynomial function from a String
	 * representation. Note:given a polynomial function represented as a double array,
	 * getPolynomFromString(poly(p)) should return an array equals to p.
	 * 
	 * @param p - a String representing polynomial function.
	 * @return
	 */
	public static double[] getPolynomFromString(String p) {
		double [] ans = ZERO;//  -1.0x^2 +3.0x +2.0
        /** add you code below

         /////////////////// */
		return ans;
	}
	/**
	 * This function computes the polynomial function which is the sum of two polynomial functions (p1,p2)
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double[] add(double[] p1, double[] p2) {
		double [] ans = ZERO;//
        /** add you code below

         /////////////////// */
		return ans;
	}
	/**
	 * This function computes the polynomial function which is the multiplication of two polynoms (p1,p2)
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double[] mul(double[] p1, double[] p2) {
		double [] ans = ZERO;//
        /** add you code below

         /////////////////// */
		return ans;
	}
	/**
	 * This function computes the derivative of the p0 polynomial function.
	 * @param po
	 * @return
	 */
	public static double[] derivative (double[] po) {
		double [] ans = ZERO;//
        /** add you code below

         /////////////////// */
		return ans;
	}
}
