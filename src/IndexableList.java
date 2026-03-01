import java.util.List;

public class IndexableList<T> {
    private Object[] data_array;
    private int[] index_array = new int[0];
    private int[] id_array = new int[0];
    private boolean init_with_size = false;
    private int indecies_to_fill = 0;

    public IndexableList() {
        data_array = new Object[0];
    }

    /**
     * Initializes the container and all its parts with the given size
     * to avoid the overhead of expanding it for each added value.
     * Use the {@code fill} method.
     * Only use this if the container is filled right away, otherwise its data contains {@code null} values!
     * @param size The initial size of the container.
     */
    public IndexableList(int size) {
        data_array = new Object[size];
        index_array = new int[data_array.length];
        id_array = new int[data_array.length];
        for (int i = 0; i < data_array.length; i++) {
            index_array[i] = i;
            id_array[i] = i;
        }
        init_with_size = true;
        indecies_to_fill = data_array.length;
    }

    public IndexableList(List<T> list) {
        data_array = list.toArray(new Object[0]);
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
    private int expand() {
        int new_len = data_array.length + 1;
        int new_index = data_array.length;
        Object[] new_arr = new Object[new_len];
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
     * Adds an element to the container if it was initialized with a size ({@code new IndexableList(int size)}).
     * Only call this to fill the container right after initialization, otherwise call {@code add}.
     * @param value The value to be added.
     * @return The ID of the added element.
     */
    public int fill(T value) {
        if (!init_with_size) {
            throw new RuntimeException("Fill method can only be used if container is initialized with size and is not filled yet.");
        }
        int index = data_array.length - indecies_to_fill;
        data_array[index] = value;
        indecies_to_fill--;
        if (indecies_to_fill == 0) {
            init_with_size = false;
        }
        return index;
    }

    /**
     * Gets the element with the given ID (not to be confused with index).
     * @param id The ID of an element is returned by the {@code add} method,
     * otherwise sequential, starting with 0, if a list or array was used to initialize this container.
     * @return The element with the given ID.
     */
    @SuppressWarnings("unchecked")
    public T get(int id) {
        return (T) data_array[index_array[id]];
    }

    /**
     * Returns all data in this container. The returned array is a clone and does not modify this container (but the data within).
     * @return The data-array in this container.
     */
    public Object[] getAll() {
        return data_array.clone();
    }

    /**
     * Expands the container and adds the element to the container and returns its ID.
     * @param value The value to be added.
     * @return The ID of the added element.
     */
    public int add(T value) {
        if (init_with_size) {
            throw new RuntimeException("Container is not fully filled after initialization with size, use the fill method instead.");
        }
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
        Object to_remove = data_array[index_array[id]];
        Object last_element = data_array[data_array.length - 1];
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
        Object[] new_data_arr = new Object[data_array.length - 1];
        System.arraycopy(data_array, 0, new_data_arr, 0, new_data_arr.length);
        data_array = new_data_arr;

        return (T) to_remove;
    }

    /**
     * Returns the number of elements in this container.
     * @return The number of elements in this container.
     */
    public int size() {
        return data_array.length;
    }
}
