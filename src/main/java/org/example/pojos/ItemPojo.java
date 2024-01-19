package org.example.pojos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPojo {

    private String name;

    private String type;

    private Boolean exotic;

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemPojo itemPojo = (ItemPojo) obj;

        var result = name.equals(itemPojo.name) && type.equals(itemPojo.type) && exotic.equals(itemPojo.exotic);

        return result;
    }
}