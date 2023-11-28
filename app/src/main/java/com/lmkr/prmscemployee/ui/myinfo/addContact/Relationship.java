package com.lmkr.prmscemployee.ui.myinfo.addContact;

public enum Relationship {
    MOTHER(1),
    FATHER(2),
    BROTHER(3),
    SISTER(4),
    SPOUSE(5),
    DAUGHTER(6),
    SON(7),
    FRIEND(8);

    private final int value;

    Relationship(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getIntegerValue(String relationshipString) {
        for (Relationship relationship : Relationship.values()) {
            if (relationship.name().equalsIgnoreCase(relationshipString)) {
                return relationship.getValue();
            }
        }
        return -1;
    }
}

