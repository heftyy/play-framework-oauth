package oauth.helper;

import com.google.common.collect.Collections2;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

import java.util.List;

public class GuiceHelper {
    public static Module subtractBinding(Module module, Key<?> toSubtract) {
        List<Element> elements = Elements.getElements(module);

        return Elements.getModule(Collections2.filter(elements, input -> {
            if (input instanceof Binding) {
                return !((Binding) input).getKey().equals(toSubtract);
            }

            return true;
        }));
    }
}
