package io.anuke.ucore.noise;

public class Interp{
   
      /// Performs cubic interpolation between two values bound between two other
      /// values.
      ///
      /// @param n0 The value before the first value.
      /// @param n1 The first value.
      /// @param n2 The second value.
      /// @param n3 The value after the second value.
      /// @param a The alpha value.
      ///
      /// @returns The interpolated value.
      ///
      /// The alpha value should range from 0.0 to 1.0.  If the alpha value is
      /// 0.0, this function returns @a n1.  If the alpha value is 1.0, this
      /// function returns @a n2.
      public static double cubicInterp (double n0, double n1, double n2, double n3, double a){
         double p = (n3 - n2) - (n0 - n1);
         double q = (n0 - n1) - p;
         double r = n2 - n0;
         double s = n1;
         return p * a * a * a + q * a * a + r * a + s;
      }

      /// Performs linear interpolation between two values.
      ///
      /// @param n0 The first value.
      /// @param n1 The second value.
      /// @param a The alpha value.
      ///
      /// @returns The interpolated value.
      ///
      /// The alpha value should range from 0.0 to 1.0.  If the alpha value is
      /// 0.0, this function returns @a n0.  If the alpha value is 1.0, this
      /// function returns @a n1.
      public static double linearInterp (double n0, double n1, double a){
        return ((1.0 - a) * n0) + (a * n1);
      }

      /// Maps a value onto a cubic S-curve.
      ///
      /// @param a The value to map onto a cubic S-curve.
      ///
      /// @returns The mapped value.
      ///
      /// @a a should range from 0.0 to 1.0.
      ///
      /// The derivative of a cubic S-curve is zero at @a a = 0.0 and @a a =
      /// 1.0
      public static double SCurve3 (double a) {
        return (a * a * (3.0 - 2.0 * a));
      }

      /// Maps a value onto a quintic S-curve.
      ///
      /// @param a The value to map onto a quintic S-curve.
      ///
      /// @returns The mapped value.
      ///
      /// @a a should range from 0.0 to 1.0.
      ///
      /// The first derivative of a quintic S-curve is zero at @a a = 0.0 and
      /// @a a = 1.0
      ///
      /// The second derivative of a quintic S-curve is zero at @a a = 0.0 and
      /// @a a = 1.0
      static double SCurve5 (double a){
        double a3 = a * a * a;
        double a4 = a3 * a;
        double a5 = a4 * a;
        return (6.0 * a5) - (15.0 * a4) + (10.0 * a3);
      }

}
