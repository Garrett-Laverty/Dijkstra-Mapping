import java.util.NoSuchElementException;

public class HeapPriorityQueue <T extends Comparable<? super T>> implements PriorityQueueInterface<T>{
	
	private T[] elements;
	private int size;
	private static final int DEFAULT_CAPACITY = 10;
	
	public HeapPriorityQueue() {
		this(DEFAULT_CAPACITY);
	}
	public HeapPriorityQueue(int initialCapacity) {
		elements = (T[]) new Comparable[initialCapacity+1];
	}
	public HeapPriorityQueue(T[] entries) {
		this(entries.length + 1);
		size = entries.length;
		for (int index = 0; index < entries.length; index++)
			elements[index + 1] = entries[index];
		for (int rIndex = size / 2; rIndex > 0; rIndex--)
			reheapDown(rIndex);
	}
	
	@Override
	public boolean isEmpty() {
		return elements[1] == null;
	}

	@Override
	public boolean isFull() {
		return elements.length == size + 1;
	}

	@Override
	public void clear() {
		for(int i = 1; i <= size; i++) {
			elements[i] = null;
		}
		size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void add(T newEntry) {
		verifyCapacity();
		elements[++size] = newEntry;
		reheapUp(size);
	}

	@Override
	public T peek() {
		if(isEmpty()) return null;
		return elements[1];
	}

	@Override
	public T remove() {
		if(isEmpty()) throw new NoSuchElementException();
		
		T data = elements[1];
		elements[1] = elements[size];
		elements[size--] = null;
		reheapDown(1);
		return data;
	}
	@Override
	public String toString() {
		String ret = "[";
		for (int i = 0; i < size; i++) {
			ret += elements[i] + ", ";
		}
		ret += elements[size];
		return ret + "]";
	}
	// ================================================================== HELPER METHODS
	private void reheapDown(int index) {
		int highest = 0;
		while(index * 2 <= size) {
			highest = getHighestPriority(index);
			if(elements[highest].compareTo(elements[index]) < 0) {
				swap(index, highest);
			}
			index = highest;
		}
	}
	private int getHighestPriority(int index) {
		int highest = 0;
		if (index * 2 + 1 > size) highest = index * 2;
		else highest = (elements[index * 2].compareTo(elements[index * 2 + 1]) < 0) ? 
				        index * 2 : index * 2 + 1;
		
		return highest;
	}
	private void reheapUp(int index) {
		int parentIndex = index / 2;
		while(parentIndex > 0 && 
		elements[index].compareTo(elements[parentIndex]) < 0) {
			swap(index, parentIndex);
			index = parentIndex;
			parentIndex = index / 2;
		}
	}
	private void swap(int index, int parentIndex) {
		T tmp = elements[index];
		elements[index] = elements[parentIndex];
		elements[parentIndex] = tmp;
	}
	private void verifyCapacity() {
		if (isFull()) {
			T[] original = elements;
			int orig_size = original.length;
			elements = (T[]) new Comparable[orig_size * 2];
			for (int i = 0; i < orig_size; i++) {
				elements[i] = original[i];
			}
		}
	}
}
