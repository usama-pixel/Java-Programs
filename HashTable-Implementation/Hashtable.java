

/**
 * A HashTable with no deletions allowed. Duplicates overwrite the existing value. Values are of
 * type V and keys are strings -- one extension is to adapt this class to use other types as keys.
 * 
 * The underlying data is stored in the array `arr', and the actual values stored are pairs of 
 * (key, value). This is so that we can detect collisions in the hash function and look for the next 
 * location when necessary.
 */
import java.math.BigInteger;
import java.util.Collection;
import java.util.ArrayList;
public class Hashtable<V> {

	private Object[] arr; //an array of Pair objects, where each pair contains the key and value stored in the hashtable
	private int max; //the size of arr. This should be a prime number
	private int itemCount; //the number of items stored in arr
	private final double maxLoad = 0.6; //the maximum load factor
    private int indx=-1;
    public void exp (String key1,String key2, V a, V b){
        // System.out.println("V: "+a);
        // new Pair("k",a);
        arr[0]=new Pair(key1,a);
        arr[11]=new Pair(key2,b);
        itemCount++;
        itemCount++;
    }
    public void exp2 (String key){
        System.out.println(this.find(0,key,1));
    }
    // exp
	public static enum PROBE_TYPE {
		LINEAR_PROBE, QUADRATIC_PROBE, DOUBLE_HASH;
	}

	PROBE_TYPE probeType; //the type of probe to use when dealing with collisions
	private final BigInteger DBL_HASH_K = BigInteger.valueOf(8);

	/**
	 * Create a new Hashtable with a given initial capacity and using a given probe type
	 * @param initialCapacity
	 * @param pt
	 */
	public Hashtable(int initialCapacity, PROBE_TYPE pt) {
        //...add code
        probeType=pt;
        max=nextPrime(initialCapacity);
        arr=new Object[max];
	}
	
	/**
	 * Create a new Hashtable with a given initial capacity and using the default probe type
	 * @param initialCapacity
	 */
	public Hashtable(int initialCapacity) {
		//...add code
        probeType=PROBE_TYPE.LINEAR_PROBE;
        max=nextPrime(initialCapacity);
        arr=new Object[max];
	}

	/**
	 * Store the value against the given key. If the loadFactor exceeds maxLoad, call the resize 
	 * method to resize the array. the If key already exists then its value should be overwritten.
	 * Create a new Pair item containing the key and value, then use the findEmpty method to find an unoccupied 
	 * position in the array to store the pair. Call findEmpty with the hashed value of the key as the starting
	 * position for the search, stepNum of zero and the original key.
	 * containing   
	 * @param key
	 * @param value
	 */
	public void put(String key, V value) {
		// throw new UnsupportedOperationException("Method not implemented");
        
        if(this.getLoadFactor()>maxLoad){
            System.out.println("Items: "+itemCount);
            System.out.println("MaxLoad: "+Double.valueOf(itemCount)/max);
            this.resize();
        }
        boolean flag=false;
        // for( int i=0;i<max;i++ ) {
        //     if(arr[i]!=null){

        //     }
        // }
        if( this.hasKey(key) ){
            for( int i=0; i<max; i++ ) {
                if(arr[i] != null && ((Pair)arr[i]).key == key ) {
                    ((Pair)arr[i]).value = value;
                    break;
                }
            }
            // this.find(hash(key),key,0);
            // System.out.println("Yo indx: "+indx);
            // ((Pair)arr[indx]).value=value;
        }
        else {
            // System.out.println("inside! ");
            // System.out.println("index "+this.findEmpty(this.hash(key),0,key));
            int emptIndex = this.findEmpty(this.hash(key),0,key);
            // System.out.println("Empt: "+emptIndex);
            arr[emptIndex] = new Pair(key,value);
            itemCount++;
        }
        // if( arr[this.hash(key)].value == value)
        // if(this.find())
        // // returns empty index
        // this.findEmpty(this.hash(key),0,key);
        // itemCount++;
        // new Pair(key,value);
	}

	/**
	 * Get the value associated with key, or return null if key does not exists. Use the find method to search the
	 * array, starting at the hashed value of the key, stepNum of zero and the original key.
	 * @param key
	 * @return
	 */
	public V get(String key) {
		// throw new UnsupportedOperationException("Method not implemented");
        V val = this.find(this.hash(key),key,0);
        if( val != null )
        return val;
        else
        return null;
	}

	/**
	 * Return true if the Hashtable contains this key, false otherwise 
	 * @param key
	 * @return
	 */
	public boolean hasKey(String key) {
		// throw new UnsupportedOperationException("Method not implemented");
        boolean hasKey = false;
		if (null != get(key)) {
			hasKey = true;
		}
		return hasKey;
        // if(this.find(this.hash(key),key,0) == null) {
        //     return false;
        // }
        // else {
        //     return true;
        // }
	}

	/**
	 * Return all the keys in this Hashtable as a collection
	 * @return
	 */
	public Collection<String> getKeys() {
		// throw new UnsupportedOperationException("Method not implemented");
        Collection collection = new ArrayList<String>();
        for(int i=0; i<max; i++) {
            if(arr[i] != null){
                collection.add(((Pair)arr[i]).key);
            }
        }
        return collection;
	}

	/**
	 * Return the load factor, which is the ratio of itemCount to max
	 * @return
	 */
	public double getLoadFactor() {
		// throw new UnsupportedOperationException("Method not implemented");
        Double d1 = Double.valueOf(itemCount);
        return d1/max;
	}

	/**
	 * return the maximum capacity of the Hashtable
	 * @return
	 */
	public int getCapacity() {
		// throw new UnsupportedOperationException("Method not implemented");
        return max;
	}
	
	/**
	 * Find the value stored for this key, starting the search at position startPos in the array. If
	 * the item at position startPos is null, the Hashtable does not contain the value, so return null. 
	 * If the key stored in the pair at position startPos matches the key we're looking for, return the associated 
	 * value. If the key stored in the pair at position startPos does not match the key we're looking for, this
	 * is a hash collision so use the getNextLocation method with an incremented value of stepNum to find 
	 * the next location to search (the way that this is calculated will differ depending on the probe type 
	 * being used). Then use the value of the next location in a recursive call to find.
	 * @param startPos
	 * @param key
	 * @param stepNum
	 * @return
	 */
	private V find(int startPos, String key, int stepNum) {
		// throw new UnsupportedOperationException("Method not implemented");
        // System.out.println("recurr");
        if( arr[startPos]==null ){
            return null;
        }
        else if( ((Pair)arr[startPos]).key.equals(key) ){
            indx=startPos;
            // System.out.println(key+" Index is: "+indx);s
            return ((Pair)arr[startPos]).value;
        }
        // System.out.println("StepNum Before: "+stepNum);
        stepNum+=1;
        // System.out.println("StepNum After: "+stepNum);
        // System.out.println("key: "+key);
        // System.out.println("StartPos: "+startPos);
        int pos = this.getNextLocation(startPos,stepNum,key);
        // System.out.println("Pos: "+pos);
        return this.find(pos,key,++stepNum);
            
	}

	/**
	 * Find the first unoccupied location where a value associated with key can be stored, starting the
	 * search at position startPos. If startPos is unoccupied, return startPos. Otherwise use the getNextLocation
	 * method with an incremented value of stepNum to find the appropriate next position to check 
	 * (which will differ depending on the probe type being used) and use this in a recursive call to findEmpty.
	 * @param startPos
	 * @param stepNum
	 * @param key
	 * @return
	 */
	// private
    public int findEmpty(int startPos, int stepNum, String key) {
		// throw new UnsupportedOperationException("Method not implemented");
        // System.out.println("recur");
        if(arr[startPos]==null){
            return startPos;
        }
        stepNum+=1;
        int pos=this.getNextLocation(startPos,stepNum,key);
        return this.findEmpty(pos,stepNum,key);
        // return 0;
	}

	/**
	 * Finds the next position in the Hashtable array starting at position startPos. If the linear
	 * probe is being used, we just increment startPos. If the double hash probe type is being used, 
	 * add the double hashed value of the key to startPos. If the quadratic probe is being used, add
	 * the square of the step number to startPos.
	 * @param i
	 * @param stepNum
	 * @param key
	 * @return
	 */
	private int getNextLocation(int startPos, int stepNum, String key) {
		int step = startPos;
		switch (probeType) {
		case LINEAR_PROBE:
			step++;
			break;
		case DOUBLE_HASH:
			step += doubleHash(key);
			break;
		case QUADRATIC_PROBE:
			step += stepNum * stepNum;
			break;
		default:
			break;
		}
		return step % max;
	}

	/**
	 * A secondary hash function which returns a small value (less than or equal to DBL_HASH_K)
	 * to probe the next location if the double hash probe type is being used
	 * @param key
	 * @return
	 */
	private int doubleHash(String key) {
		BigInteger hashVal = BigInteger.valueOf(key.charAt(0) - 96);
		for (int i = 0; i < key.length(); i++) {
			BigInteger c = BigInteger.valueOf(key.charAt(i) - 96);
			hashVal = hashVal.multiply(BigInteger.valueOf(27)).add(c);
		}
		return DBL_HASH_K.subtract(hashVal.mod(DBL_HASH_K)).intValue();
	}

	/**
	 * Return an int value calculated by hashing the key. See the lecture slides for information
	 * on creating hash functions. The return value should be less than max, the maximum capacity 
	 * of the array
	 * @param key
	 * @return
	 */
	// private
    public int hash(String key) {
		// throw new UnsupportedOperationException("Method not implemented");
        int sum=0;
        for(int i=0; i<key.length(); i++) {
            sum+=(int)key.charAt(i);
        }
        
        return sum % max;
	}

	/**
	 * Return true if n is prime
	 * @param n
	 * @return
	 */
    private boolean isPrime(int n) {
		// throw new UnsupportedOperationException("Method not implemented");
        boolean flag = false;
        for (int i = 2; i <= n / 2; ++i) {
            // condition for nonprime number
            if (n % i == 0) {
                flag = true;
                break;
            }
        }
        if (!flag)
        return true;
        else
        return false;
	}

	/**
	 * Get the smallest prime number which is larger than n
	 * @param n
	 * @return
	 */
    // private
    public int nextPrime(int n) {
		// throw new UnsupportedOperationException("Method not implemented");
        for(int i=n+1;; i++) {
            boolean checkPrime = this.isPrime(i);
            // System.out.println("Check: "+checkPrime);
            if(checkPrime){
                // System.out.println(i);
                return i;
            }
            
        }
        
	}

	/**
	 * Resize the hashtable, to be used when the load factor exceeds maxLoad. The new size of
	 * the underlying array should be the smallest prime number which is at least twice the size
	 * of the old array.
	 */
	// private
    public void resize() {
		// throw new UnsupportedOperationException("Method not implemented");
        max=this.nextPrime(max*2);
        Object obj[]=new Object[max];
        for(int i=0; i<arr.length; i++) {
            if(arr[i] != null) {
                obj[i]=arr[i];
            }
        }
        arr=obj;
        obj=null;
        
        // System.out.println("After: "+arr.length);
	}

	
	/**
	 * Instances of Pair are stored in the underlying array. We can't just store
	 * the value because we need to check the original key in the case of collisions.
	 * @author jb259
	 *
	 */
	private class Pair {
		private String key;
		private V value;

		public Pair(String key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}

class Check{
	public static void main(String args[]){
        // Hashtable<Boolean> h = new Hashtable<Boolean>(1000, Hashtable.PROBE_TYPE.QUADRATIC_PROBE);
        // for(int i=0;i<2000;i++) {
		// 	for(int j=2000;j>0;j--) {
		// 		h.put(i+":"+j, true);
		// 	}
		// }
		Hashtable<String> h = new Hashtable<String>(9);
		for(int i=0;i<10;i++) {
			for(int j=10;j>0;j--) {
                // System.out.println("h.put("+i+"+\":\"+"+j+", "+j+"+\":\"+"+i+");");
				h.put(i+":"+j, j+":"+i);
			}
		}
        {
            // h.put(7+":"+7, 7+":"+7);
            // h.put(7+":"+6, 6+":"+7);
            // h.put(7+":"+5, 5+":"+7);
            // h.put(7+":"+4, 4+":"+7);
            // h.put(7+":"+3, 3+":"+7);
            // h.put(7+":"+2, 2+":"+7);
            // h.put(7+":"+1, 1+":"+7);
            // h.put(8+":"+10, 10+":"+8);
            // h.put(8+":"+9, 9+":"+8);
            // h.put(8+":"+8, 8+":"+8);
            // h.put(8+":"+7, 7+":"+8);
            // h.put(8+":"+6, 6+":"+8);
            // h.put(8+":"+5, 5+":"+8);
            // h.put(8+":"+4, 4+":"+8);
            // h.put(8+":"+3, 3+":"+8);
            // h.put(8+":"+2, 2+":"+8);
            // h.put(8+":"+1, 1+":"+8);
            // h.put(9+":"+10, 10+":"+9);
            // h.put(9+":"+9, 9+":"+9);
            // h.put(9+":"+8, 8+":"+9);
            // h.put(9+":"+7, 7+":"+9);
            // h.put(9+":"+6, 6+":"+9);
            // h.put(9+":"+5, 5+":"+9);
            // h.put(9+":"+4, 4+":"+9);
            // h.put(9+":"+3, 3+":"+9);
            // h.put(9+":"+2, 2+":"+9);
            // h.put(9+":"+1, 1+":"+9);
        }
        // h.put(0+":"+10, 10+":"+0);
        // h.put(10+":"+0, 0+":"+10);
		System.out.println(h.get(0+":"+10));
		// System.out.println(h.get(10+":"+0));

		// for(int i=0;i<10;i++) {
		// 	for(int j=10;j>0;j--) {
		// 		System.out.println(h.get(i+":"+j));
		// 	}
		// }
	}
}