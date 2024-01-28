package ru.edu.otus.architecture.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class QuadraticEquationSolverTest {

    public QuadraticEquationSolver solver;

    @BeforeEach
    void init() {
        solver = new QuadraticEquationSolver();
    }

    @Test
    void whenDiscriminantLessThanZeroThenEmptyArray() {
        double[] answer = solver.solve(1d, 0d, 1d);
        assertArrayEquals(new double[]{}, answer);
    }

    @Test
    void whenDiscriminantGreaterThanZeroThenTwoSolutions() {
        double[] answer = solver.solve(1d, 0d, -1d);
        assertArrayEquals(new double[]{1, -1}, answer);
    }

    @Test
    void whenDiscriminantEqualsToZeroThenOneSolution() {
        double[] answer = solver.solve(1d, 2d, 1d);
        assertArrayEquals(new double[]{-1}, answer);
    }

    @Test
    void whenDiscriminantApproximatelyEqualsToZeroThenOneSolution() {
        double[] answer = solver.solve(1d, 2.0000001d, 1d);
        assertArrayEquals(new double[]{-1.00000005d}, answer);
    }

    @Test
    void whenAEqualsZeroThanException() {
        assertThrows(IllegalArgumentException.class, () -> solver.solve(0d, 1d, 1d));
    }

    @ParameterizedTest
    @MethodSource("provideCoefficientsForQuadraticEquation")
    void whenCoefficientIsNan(double a, double b, double c) {
        assertThrows(IllegalArgumentException.class, () -> solver.solve(a, b, c));
    }

    private static Stream<Arguments> provideCoefficientsForQuadraticEquation() {
        return Stream.of(
                Arguments.of(1.0d, 1.0d, Double.NaN),
                Arguments.of(1.0d, Double.NaN, 1.0d),
                Arguments.of(Double.NaN, 1.0d, 1.0d),
                Arguments.of(Double.NaN, Double.NaN, Double.NaN)
        );
    }
}