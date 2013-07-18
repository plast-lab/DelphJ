/*
 * Random.java
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, California 95054, U.S.A.  All rights reserved.  
 * 
 * Sun Microsystems, Inc. has intellectual property rights relating to
 * technology embodied in the product that is described in this
 * document.  In particular, and without limitation, these
 * intellectual property rights may include one or more of the
 * U.S. patents listed at http://www.sun.com/patents and one or more
 * additional patents or pending patent applications in the U.S. and
 * in other countries.
 * 
 * U.S. Government Rights - Commercial software.
 * Government users are subject to the Sun Microsystems, Inc. standard
 * license agreement and applicable provisions of the FAR and its
 * supplements.  Use is subject to license terms.  Sun, Sun
 * Microsystems, the Sun logo and Java are trademarks or registered
 * trademarks of Sun Microsystems, Inc. in the U.S. and other
 * countries.  
 * 
 * This product is covered and controlled by U.S. Export Control laws
 * and may be subject to the export or import laws in other countries.
 * Nuclear, missile, chemical biological weapons or nuclear maritime
 * end uses or end users, whether direct or indirect, are strictly
 * prohibited.  Export or reexport to countries subject to
 * U.S. embargo or to entities identified on U.S. export exclusion
 * lists, including, but not limited to, the denied persons and
 * specially designated nationals lists is strictly prohibited.
 */

package examples.dstm2fromClass.util;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Lightweight random number generator.  <I>Not thread-safe.</I> Synchronization in the
 * <CODE>java.util.Randome</CODE> can distort the performance of multithreaded benchmarks.
 */
public class Random extends java.util.Random {
  
  /** use serialVersionUID from JDK 1.1 for interoperability */
  static final long serialVersionUID = 3905348978240129619L;
  
  private long seed;
  
  private final static long multiplier = 0x5DEECE66DL;
  private final static long addend = 0xBL;
  private final static long mask = (1L << 48) - 1;
  
  /**
   * Creates a new random number generator. This constructor sets
   * the seed of the random number generator to a value very likely
   * to be distinct from any other invocation of this constructor.
   */
  public Random() { this(++seedUniquifier + System.nanoTime()); }
  private static volatile long seedUniquifier = 8682522807148012L;
  
  /**
   * Creates a new random number generator using a single
   * <code>long</code> seed:
   * <blockquote><pre>
   * public Random(long seed) { setSeed(seed); }</pre></blockquote>
   * Used by method <tt>next</tt> to hold
   * the state of the pseudorandom number generator.
   *
   * @param   seed   the initial seed.
   * @see     java.util.Random#setSeed(long)
   */
  public Random(long seed) {
    this.seed = 0L;
    setSeed(seed);
  }
  
  /**
   * Sets the seed of this random number generator using a single
   * <code>long</code> seed. The general contract of <tt>setSeed</tt>
   * is that it alters the state of this random number generator
   * object so as to be in exactly the same state as if it had just
   * been created with the argument <tt>seed</tt> as a seed. The method
   * <tt>setSeed</tt> is implemented by class Random as follows:
   * <blockquote><pre>
   * synchronized public void setSeed(long seed) {
   *       this.seed = (seed ^ 0x5DEECE66DL) & ((1L << 48) - 1);
   *       haveNextNextGaussian = false;
   * }</pre></blockquote>
   * The implementation of <tt>setSeed</tt> by class <tt>Random</tt>
   * happens to use only 48 bits of the given seed. In general, however,
   * an overriding method may use all 64 bits of the long argument
   * as a seed value.
   *
   * Note: Although the seed value is an AtomicLong, this method
   *       must still be synchronized to ensure correct semantics
   *       of haveNextNextGaussian.
   *
   * @param   seed   the initial seed.
   */
  public void setSeed(long seed) {
    seed = (seed ^ multiplier) & mask;
    this.seed = seed;
    haveNextNextGaussian = false;
  }
  
  /**
   * Generates the next pseudorandom number. Subclass should
   * override this, as this is used by all other methods.<p>
   * The general contract of <tt>next</tt> is that it returns an
   * <tt>int</tt> value and if the argument bits is between <tt>1</tt>
   * and <tt>32</tt> (inclusive), then that many low-order bits of the
   * returned value will be (approximately) independently chosen bit
   * values, each of which is (approximately) equally likely to be
   * <tt>0</tt> or <tt>1</tt>. The method <tt>next</tt> is implemented
   * by class <tt>Random</tt> as follows:
   * <blockquote><pre>
   * synchronized protected int next(int bits) {
   *       seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
   *       return (int)(seed >>> (48 - bits));
   * }</pre></blockquote>
   * This is a linear congruential pseudorandom number generator, as
   * defined by D. H. Lehmer and described by Donald E. Knuth in <i>The
   * Art of Computer Programming,</i> Volume 2: <i>Seminumerical
   * Algorithms</i>, section 3.2.1.
   *
   * @param   bits random bits
   * @return  the next pseudorandom value from this random number generator's sequence.
   * @since   JDK1.1
   */
  protected int next(int bits) {
    seed = (seed * multiplier + addend) & mask;
    return (int)(seed >>> (48 - bits));
  }
  
  private static final int BITS_PER_BYTE = 8;
  private static final int BYTES_PER_INT = 4;
  
  private double nextNextGaussian;
  private boolean haveNextNextGaussian = false;
  
  /**
   * Returns the next pseudorandom, Gaussian ("normally") distributed
   * <code>double</code> value with mean <code>0.0</code> and standard
   * deviation <code>1.0</code> from this random number generator's sequence.
   * <p>
   * The general contract of <tt>nextGaussian</tt> is that one
   * <tt>double</tt> value, chosen from (approximately) the usual
   * normal distribution with mean <tt>0.0</tt> and standard deviation
   * <tt>1.0</tt>, is pseudorandomly generated and returned. The method
   * <tt>nextGaussian</tt> is implemented by class <tt>Random</tt> as follows:
   * <blockquote><pre>
   * synchronized public double nextGaussian() {
   *    if (haveNextNextGaussian) {
   *            haveNextNextGaussian = false;
   *            return nextNextGaussian;
   *    } else {
   *            double v1, v2, s;
   *            do {
   *                    v1 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
   *                    v2 = 2 * nextDouble() - 1;   // between -1.0 and 1.0
   *                    s = v1 * v1 + v2 * v2;
   *            } while (s >= 1 || s == 0);
   *            double multiplier = Math.sqrt(-2 * Math.log(s)/s);
   *            nextNextGaussian = v2 * multiplier;
   *            haveNextNextGaussian = true;
   *            return v1 * multiplier;
   *    }
   * }</pre></blockquote>
   * This uses the <i>polar method</i> of G. E. P. Box, M. E. Muller, and
   * G. Marsaglia, as described by Donald E. Knuth in <i>The Art of
   * Computer Programming</i>, Volume 2: <i>Seminumerical Algorithms</i>,
   * section 3.4.1, subsection C, algorithm P. Note that it generates two
   * independent values at the cost of only one call to <tt>Math.log</tt>
   * and one call to <tt>Math.sqrt</tt>.
   *
   * @return  the next pseudorandom, Gaussian ("normally") distributed
   *          <code>double</code> value with mean <code>0.0</code> and
   *          standard deviation <code>1.0</code> from this random number
   *          generator's sequence.
   */
  public double nextGaussian() {
    // See Knuth, ACP, Section 3.4.1 Algorithm C.
    if (haveNextNextGaussian) {
      haveNextNextGaussian = false;
      return nextNextGaussian;
    } else {
      double v1, v2, s;
      do {
        v1 = 2 * nextDouble() - 1; // between -1 and 1
        v2 = 2 * nextDouble() - 1; // between -1 and 1
        s = v1 * v1 + v2 * v2;
      } while (s >= 1 || s == 0);
      double multiplier = Math.sqrt(-2 * Math.log(s)/s);
      nextNextGaussian = v2 * multiplier;
      haveNextNextGaussian = true;
      return v1 * multiplier;
    }
  }
  
  /**
   * Serializable fields for Random.
   *
   * @serialField    seed long;
   *              seed for random computations
   * @serialField    nextNextGaussian double;
   *              next Gaussian to be returned
   * @serialField      haveNextNextGaussian boolean
   *              nextNextGaussian is valid
   */
  private static final ObjectStreamField[] serialPersistentFields = {
    new ObjectStreamField("seed", Long.TYPE),
        new ObjectStreamField("nextNextGaussian", Double.TYPE),
        new ObjectStreamField("haveNextNextGaussian", Boolean.TYPE)
  };
  
  /**
   * Reconstitute the <tt>Random</tt> instance from a stream (that is,
   * deserialize it). The seed is read in as long for
   * historical reasons, but it is converted to an AtomicLong.
   */
  private void readObject(java.io.ObjectInputStream s)
  throws java.io.IOException, ClassNotFoundException {
    
    ObjectInputStream.GetField fields = s.readFields();
    long seedVal;
    
    seedVal = (long) fields.get("seed", -1L);
    if (seedVal < 0)
      throw new java.io.StreamCorruptedException(
          "Random: invalid seed");
    seed = seedVal;
    nextNextGaussian = fields.get("nextNextGaussian", 0.0);
    haveNextNextGaussian = fields.get("haveNextNextGaussian", false);
  }
  
  
  /**
   * Save the <tt>Random</tt> instance to a stream.
   * The seed of a Random is serialized as a long for
   * historical reasons.
   *
   */
  synchronized private void writeObject(ObjectOutputStream s) throws IOException {
    // set the values of the Serializable fields
    ObjectOutputStream.PutField fields = s.putFields();
    fields.put("seed", seed);
    fields.put("nextNextGaussian", nextNextGaussian);
    fields.put("haveNextNextGaussian", haveNextNextGaussian);
    
    // save them
    s.writeFields();
    
  }
  
}
