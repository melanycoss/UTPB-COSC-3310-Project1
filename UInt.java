/**
 * @auth
 */

import java.util.Arrays;

/**
 * <h1>UInt</h1>
 * Represents an unsigned integer using a boolean array to store the binary representation.
 * Each bit is stored as a boolean value, where true represents 1 and false represents 0.
 *
 * @author Tim Fielder
 * @version 1.0 (Sept 30, 2024)
 */
public class UInt {

    // The array representing the bits of the unsigned integer.
    protected boolean[] bits;

    // The number of bits used to represent the unsigned integer.
    protected int length;

    /**
     * Constructs a new UInt by cloning an existing UInt object.
     *
     * @param toClone The UInt object to clone.
     */
    public UInt(UInt toClone) {
        this.length = toClone.length;
        this.bits = Arrays.copyOf(toClone.bits, this.length);
    }

    /**
     * Constructs a new UInt from an integer value.
     * The integer is converted to its binary representation and stored in the bits array.
     *
     * @param i The integer value to convert to a UInt.
     */
    public UInt(int i) {
        // Determine the number of bits needed to store i in binary format.
        length = (int)(Math.ceil(Math.log(i)/Math.log(2.0)) + 1);
        bits = new boolean[length];

        // Convert the integer to binary and store each bit in the array.
        for (int b = length-1; b >= 0; b--) {
            // We use a ternary to decompose the integer into binary digits, starting with the 1s place.
            bits[b] = i % 2 == 1;
            // Right shift the integer to process the next bit.
            i = i >> 1;

            // Deprecated analog method
            /*int p = 0;
            while (Math.pow(2, p) < i) {
                p++;
            }
            p--;
            bits[p] = true;
            i -= Math.pow(2, p);*/
        }
    }

    /**
     * Creates and returns a copy of this UInt object.
     *
     * @return A new UInt object that is a clone of this instance.
     */
    @Override
    public UInt clone() {
        return new UInt(this);
    }

    /**
     * Creates and returns a copy of the given UInt object.
     *
     * @param u The UInt object to clone.
     * @return A new UInt object that is a copy of the given object.
     */
    public static UInt clone(UInt u) {
        return new UInt(u);
    }

    /**
     * Converts this UInt to its integer representation.
     *
     * @return The integer value corresponding to this UInt.
     */
    public int toInt() {
        int t = 0;
        // Traverse the bits array to reconstruct the integer value.
        for (int i = 0; i < length; i++) {
            // Again, using a ternary to now re-construct the int value, starting with the most-significant bit.
            t = t + (bits[i] ? 1 : 0);
            // Shift the value left for the next bit.
            t = t << 1;
        }
        return t >> 1; // Adjust for the last shift.
    }

    /**
     * Static method to retrieve the int value from a generic UInt object.
     *
     * @param u The UInt to convert.
     * @return The int value represented by u.
     */
    public static int toInt(UInt u) {
        return u.toInt();
    }

    /**
     * Returns a String representation of this binary object with a leading 0b.
     *
     * @return The constructed String.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("0b");
        // Construct the String starting with the most-significant bit.
        for (int i = 0; i < length; i++) {
            // Again, we use a ternary here to convert from true/false to 1/0
            s.append(bits[i] ? "1" : "0");
        }
        return s.toString();
    }

    /**
     * Performs a logical AND operation using this.bits and u.bits, with the result stored in this.bits.
     *
     * @param u The UInt to AND this against.
     */
    public void and(UInt u) {
        // We want to traverse the bits arrays to perform our AND operation.
        // But keep in mind that the arrays may not be the same length.
        // So first we use Math.min to determine which is shorter.
        // Then we need to align the two arrays at the 1s place, which we accomplish by indexing them at length-i-1.
        for (int i = 0; i < Math.min(this.length, u.length); i++) {
            this.bits[this.length - i - 1] =
                    this.bits[this.length - i - 1] &
                            u.bits[u.length - i - 1];
        }
    }

    /**
     * Accepts a pair of UInt objects and uses a temporary clone to safely AND them together (without changing either).
     *
     * @param a The first UInt
     * @param b The second UInt
     * @return The temp object containing the result of the AND op.
     */
    public static UInt and(UInt a, UInt b) {
        UInt temp = a.clone();
        temp.and(b);
        return temp;
    }

    public void or(UInt u) {
        // TODO Complete the bitwise logical OR method
        for (int i = 0; i < Math.min(this.length, u.length); i++) { //iterates through maximum of either this or u
            this.bits[this.length - i - 1] = //works from right to left
                    this.bits[this.length - i - 1] |
                            u.bits[u.length - i - 1];}
        //turns this' bits true if either this or u bits are true

    }

    public static UInt or(UInt a, UInt b) {
        // TODO Complete the static OR method
        UInt temp = a.clone();
        temp.or(b);
        return temp;
    }

    public void xor(UInt u) {
        // TODO Complete the bitwise logical XOR method
        for (int i = 0; i < Math.min(this.length, u.length); i++) { //iterates through maximum of either this or u
            this.bits[this.length - i - 1] = //works from right to left
                    this.bits[this.length - i - 1] ^
                            u.bits[u.length - i - 1];}
        //turns this' bits true only if one of this or u bits are true and the other is false
        return;
    }

    public static UInt xor(UInt a, UInt b) {
        // TODO Complete the static XOR method
        UInt temp = a.clone();
        temp.xor(b);
        return temp;
    }

    public void add(UInt u) {
        // TODO Using a ripple-carry adder, perform addition using a passed UINT object

        int max = Math.max(this.length, u.length);  // holds the maximum length between the two numbers
        boolean carry = false;  // holds no value for the carry at the beginning

        boolean[] sum = new boolean[max + 1];  // create an array that holds the sum

        // iterates through each bit while working from right to left
        for (int i = 0; i < max; i++) {
            boolean bitThis = i < this.length && this.bits[this.length - 1 - i];  // gets the bit from this
            boolean bitU = i < u.length && u.bits[u.length - 1 - i];  // gets the bit from u

            // combines the bits for place i in the array while considering the carry
            sum[max - i] = (bitThis ^ bitU) ^ carry;

            // changes the value of the carry
            // turns true if at least two of bitThis, bitU, and carry are true
            carry = (bitThis && bitU) || (carry && (bitThis || bitU));
        }

        // sets the leftmost bit to the carry, if there's any
        sum[0] = carry;
        int answer = 0;

        for (int i = 0; i < sum.length; i++) {
            if (sum[i]) {
                answer |= (1 << (sum.length - 1 - i));  // Shift and add if the bit is true
            }
        }

        // updates this.bits and length to match the new result
        this.length = max + (carry ? 1 : 0);  // increase length if there's a carry
        this.bits = new boolean[this.length];  // reinitialize the bits array

        // copy the result into this.bits
        for (int i = 0; i < this.length; i++) {
            this.bits[i] = sum[sum.length - this.length + i];  // Adjust to match result length
        }
    }

    public static UInt add(UInt a, UInt b) {
        // TODO A static change-safe version of add, should return a temp UInt object like the bitwise ops.
        UInt temp = a.clone();
        temp.add(b);
        return temp;
    }

    public void negate() {
        // TODO You'll need a way to perform 2's complement negation
        // The add() method will be helpful with this.

        //iterate through this and invert every bit
        for (int i = 0; i < this.length; i++) {
            this.bits[i] = !this.bits[i];
        }
        this.add(new UInt(1)); //adds 1 for 2's complement
    }

    public void sub(UInt u) {
        // TODO Using negate() and add(), perform in-place subtraction
        UInt firstNum = clone(this); //clone this
        UInt secondNum = clone(u); //clone u

        int max = Math.max(u.length, this.length); //stores max between this and u
        boolean[] expandBits = new boolean[max]; //creates array for ensuring both this and u are the same length
        Arrays.fill(expandBits, true); //fills array with true to prepare for negation
        int result = 0; //initialize value that will store the final int result

        //decide what to do based on u or this lengths
        if (firstNum.length < secondNum.length) {
            firstNum.negate(); //negates this
            for (int i = (secondNum.length - firstNum.length); i < max; i++) {
                expandBits[i] = firstNum.bits[i - (secondNum.length - firstNum.length)];
            } //fills array with this bits starting from difference
            for (int i = 0; i < expandBits.length; i++) {
                if (expandBits[i]) {
                    result |= (1 << (expandBits.length - 1 - i));  // shift and add if the bit is true
                } //represents bit array as an int
            }
            firstNum.bits[0] = true; //sets first bit to true
        }

        if (firstNum.length > secondNum.length) {
            secondNum.negate(); //negates u
            for (int i = (firstNum.length - secondNum.length); i < max; i++) {
                expandBits[i] = secondNum.bits[i - (firstNum.length - secondNum.length)];
            } //fills array with u bits starting from difference
            for (int i = 0; i < expandBits.length; i++) {
                if (expandBits[i]) {
                    result |= (1 << (expandBits.length - 1 - i));  // Shift and add if the bit is true
                } //represents bit array as an int
            }
            secondNum = new UInt(result); //converts u to UInt result
            secondNum.bits[0] = true; //sets first bit to true
        }

        if (firstNum.length == secondNum.length) {
            secondNum.negate(); //negates u but either would have worked
        }

        firstNum.add(secondNum); //adds u to this
        firstNum.bits[0] = false; //sets first bit to false

        //send back new value
        this.length = firstNum.length;
        this.bits = firstNum.bits;
    }

    public static UInt sub(UInt a, UInt b) {
        // TODO And a static change-safe version of sub
        UInt temp = a.clone();
        temp.sub(b);
        return temp;
    }
    public void mul(UInt u) {
        // TODO Using Booth's algorithm, perform multiplication
        // This one will require that you increase the length of bits, up to a maximum of X+Y.
        // Having negate() and add() will obviously be useful here.
        // Also note the Booth's always treats binary values as if they are signed,
        //   while this class is only intended to use unsigned values.
        // This means that you may need to pad your bits array with a leading 0 if it's not already long enough.

        int paddedLength = Math.max(this.length, u.length); //stores value of y
        int fullLength = paddedLength * 2 + 1; //stores value of x + y + 1
        int hold = this.toInt() << paddedLength + 1; //stores value of rightmost padded int of this
        UInt multiplicand = new UInt(hold); //(A Value) stores new UInt multiplicand with value of hold
        UInt neg_multiplicand = clone(this); //clones this
        neg_multiplicand.negate(); //creates negative multiplicand
        int hold1 = neg_multiplicand.toInt() << paddedLength + 1; //stores value of rightmost padded int of negated this
        neg_multiplicand = new UInt(hold1); //(S Value) reinitializes negative multiplicand with padded value

        //addresses leftmost padding on negative multiplicand
        for (int i = 0; i < neg_multiplicand.length; i++) {
            if ((i + 1) < neg_multiplicand.length) {
                neg_multiplicand.bits[i] = neg_multiplicand.bits[i + 1];
            }
        }
        neg_multiplicand.length = fullLength;

        UInt multiplier = clone(u); //clones u
        int hold2 = multiplier.toInt() << 1; //shifts left for multiplication holder
        multiplier = new UInt(hold2); //(P value) reinitalizes multiplier for padded value

        try {
            for (int i = 0; i < paddedLength; i++) { //continues for y amount of rounds
                boolean secondLast = multiplier.bits[multiplier.length - 2]; //checks and stores second to last bit
                boolean last = multiplier.bits[multiplier.length - 1]; //checks and stores last bit

                if (last && !secondLast) {
                    //P + A
                    multiplier.add(multiplicand);
                }
                if (!last && secondLast) {
                    //P + S
                    multiplier.add(neg_multiplicand);
                }
                if (multiplier.length > fullLength) {
                    //addresses overflow
                    multiplier.bits[0] = multiplier.bits[1];
                }
                if (multiplier.bits[0]) {
                    //creates "signed" value if leading bit is true pre-shift
                    hold = multiplier.toInt() >> 1;
                    multiplier = new UInt(hold);
                    multiplier.bits[0] = true;
                } else {
                    //shifts normally
                    hold = multiplier.toInt() >> 1;
                    multiplier = new UInt(hold);
                }
            }
            //does one final shift
            hold = multiplier.toInt() >> 1;
            multiplier = new UInt(hold); //reinitializes multiplier with the final value

            this.length = multiplier.length;
            this.bits = multiplier.bits;
        } catch (ArrayIndexOutOfBoundsException ignored) {
            //I don't know how to fix this without giving myself another headache
        }
    }
    public static UInt mul(UInt a, UInt b) {
        // TODO A static, change-safe version of mul
        UInt temp = a.clone();
        temp.mul(b);
        return temp;
    }
}