import java.util.List;

public class IndexableList<T> {
    private T[] data_array;
    private int[] index_array = new int[0];
    private int[] id_array = new int[0];

    @SuppressWarnings("unchecked")
    public IndexableList() {
        data_array = (T[]) new Object[0];
    }

    /**
     * Initializes the container and all its parts with the given size
     * to avoid the overhead of expanding it for each added value.
     * Only use this if the container is filled right away, otherwise its data contains {@code null} values!
     * @param size The initial size of the container.
     */
    @SuppressWarnings("unchecked")
    public IndexableList(int size) {
        data_array = (T[]) new Object[size];
        index_array = new int[data_array.length];
        id_array = new int[data_array.length];
        for (int i = 0; i < data_array.length; i++) {
            index_array[i] = i;
            id_array[i] = i;
        }
    }

    @SuppressWarnings("unchecked")
    public IndexableList(List<T> list) {
        data_array = (T[]) list.toArray(new Object[0]);
        index_array = new int[data_array.length];
        id_array = new int[data_array.length];
        for (int i = 0; i < data_array.length; i++) {
            index_array[i] = i;
            id_array[i] = i;
        }
    }

    /**
     * Initialize this container with an array.
     * @param array Must be a non-primitive.
     */
    public IndexableList(T[] array) {
        data_array = array.clone();
        index_array = new int[data_array.length];
        id_array = new int[data_array.length];
        for (int i = 0; i < data_array.length; i++) {
            index_array[i] = i;
            id_array[i] = i;
        }
    }

    /**
     * Expands all necessary arrays and returns the newly created index.
     * @return {@code int} The newly created index.
     */
    @SuppressWarnings("unchecked")
    private int expand() {
        int new_len = data_array.length + 1;
        int new_index = data_array.length;
        T[] new_arr = (T[]) new Object[new_len];
        System.arraycopy(data_array, 0, new_arr, 0, data_array.length);
        data_array = new_arr;

        if (index_array.length < new_len) {
            int[] new_idx = new int[new_len];
            System.arraycopy(index_array, 0, new_idx, 0, index_array.length);
            new_idx[new_index] = new_index;
            index_array = new_idx;

            int[] new_id = new int[new_len];
            System.arraycopy(id_array, 0, new_id, 0, id_array.length);
            new_id[new_index] = new_index;
            id_array = new_id;
        }

        return new_index;
    }

    /**
     * Gets the element with the given ID (not to be confused with index).
     * @param id The ID of an element is returned by the {@code add} method,
     * otherwise sequential, starting with 0, if a list or array was used to initialize this container.
     * @return The element with the given ID.
     */
    public T get(int id) {
        return data_array[index_array[id]];
    }

    /**
     * Returns all data in this container. The returned array is a clone and does not modify this containers data.
     * @return The data-array in this container.
     */
    public T[] getAll() {
        return data_array.clone();
    }

    /**
     * Expands the container and adds the element to the container and returns its ID.
     * @param value The value to be added.
     * @return The ID of the added element.
     */
    public int add(T value) {
        int index = expand();
        data_array[index] = value;
        return id_array[index];
    }

    /**
     * Removes the element with the given ID (not to be confused with index) and returns it.
     * Keeps track and rearranges tracking components to ensure indexability.
     * @param id The ID of an element is returned by the {@code add} method,
     * otherwise sequential, starting with 0, if a list or array was used to initialize this container.
     * @return The removed element.
     */
    @SuppressWarnings("unchecked")
    public T remove(int id) {
        T to_remove = data_array[index_array[id]];
        T last_element = data_array[data_array.length - 1];
        int to_remove_index = index_array[id];
        int last_index = data_array.length - 1;
        data_array[to_remove_index] = last_element;
        data_array[last_index] = to_remove;
        int to_remove_id = id_array[to_remove_index];
        int last_id = id_array[last_index];
        id_array[to_remove_index] = last_id;
        id_array[last_index] = to_remove_id;
        index_array[id_array[to_remove_index]] = to_remove_index;
        index_array[id_array[last_index]] = last_index;
        T[] new_data_arr = (T[]) new Object[data_array.length - 1];
        System.arraycopy(data_array, 0, new_data_arr, 0, new_data_arr.length);
        data_array = new_data_arr;

        return to_remove;
    }

    /**
     * Returns the number of elements in this container.
     * @return The number of elements in this container.
     */
    public int size() {
        return data_array.length;
    }
}
