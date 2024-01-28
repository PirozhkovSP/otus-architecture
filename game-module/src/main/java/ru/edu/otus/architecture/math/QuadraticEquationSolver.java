package ru.edu.otus.architecture.math;

public class QuadraticEquationSolver {

    public double[] solve(double a, double b, double c) {
        if (!(isNumber(a) && isNumber(b) && isNumber(c)))
            throw new IllegalArgumentException("Some coefficients are invalid");

        if (isZero(a)) throw new IllegalArgumentException("Coefficient equal to zero");

        double discriminant = Math.pow(b, 2) - 4 * a * c;

        if (isZero(discriminant)) return new double[] {(-1) * b / 2 / a};
        if (discriminant < 0) return new double[]{};
        return new double[] {((-1) * b + Math.sqrt(discriminant)) / 2 / a,
                ((-1) * b - Math.sqrt(discriminant)) / 2 / a};
    }

    private static boolean isNumber(double number) {
        return !Double.isNaN(number) && Double.isFinite(number);
    }

    private static boolean isZero(double value, double threshold){
        return value >= -threshold && value <= threshold;
    }

    private static boolean isZero(double value){
        return isZero(value, .000001);
    }
}
