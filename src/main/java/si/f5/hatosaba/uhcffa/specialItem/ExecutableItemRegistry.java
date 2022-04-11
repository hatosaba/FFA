package si.f5.hatosaba.uhcffa.specialItem;

import java.util.HashMap;

public class ExecutableItemRegistry {

    private final HashMap<String, ExecutableItem> executableItems = new HashMap<>();

    public void register(ExecutableItem item) {
        executableItems.put(item.id, item);
    }

    public void unregister(ExecutableItem item) {
        executableItems.remove(item.id);
    }

    public boolean isRegistered(ExecutableItem item) {
        return executableItems.containsKey(item.id);
    }

    ExecutableItem get(String executableItemId) {
        return executableItems.get(executableItemId);
    }

}
