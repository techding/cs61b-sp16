package synthesizer;

import java.util.ArrayList;

//Make sure this class is public
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        //       Create a buffer with capacity = SR / frequency. You'll need to
        //       cast the result of this divsion operation into an int. For better
        //       accuracy, use the Math.round() function before casting.
        //       Your buffer should be initially filled with zeros.
        int cap = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(cap);
        //fill it with 0
        for (int i = 0; i < cap; i++) {
            buffer.enqueue(0.0);
        }


    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        // Dequeue everything in the buffer, and replace it with random numbers
        //       between -0.5 and 0.5. You can get such a number by using:
        //       double r = Math.random() - 0.5;
        //
        //       Make sure that your random numbers are different from each other. How?
        int fillCount = buffer.fillCount();
        for (int i = 0; i < fillCount; i++) {
            buffer.dequeue();
        }

        ArrayList<Double> numbers = new ArrayList<>();
        while (numbers.size() < buffer.capacity()) {
            double r = Math.random() - 0.5;
            if (!numbers.contains(r)) {
                numbers.add(r);
            }
        }

        for (double r : numbers) {
            buffer.enqueue(r);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        // Dequeue the front sample and enqueue a new sample that is
        //       the average of the two multiplied by the DECAY factor.
        //       Do not call StdAudio.play().
        double old = buffer.dequeue();
        double n = (old + buffer.peek()) / 2 * 0.996;
        buffer.enqueue(n);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        // Return the correct thing.
        return buffer.peek();
    }
}
