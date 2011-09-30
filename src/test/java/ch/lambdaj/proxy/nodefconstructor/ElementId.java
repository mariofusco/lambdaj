package ch.lambdaj.proxy.nodefconstructor;

public class ElementId {
    private String elementID;

    public ElementId(final String elementID) {
        this.elementID = elementID;
    }

    public String getString() {
        return elementID;
    }

    public int intValue() {
        return Integer.parseInt(elementID);
    }

    public void setValue(final String id) {
        elementID = id;
    }

    @Override
    public String toString() {
        return elementID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((elementID == null) ? 0 : elementID.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ElementId other = (ElementId) obj;
        if (elementID == null) {
            if (other.elementID != null) {
                return false;
            }
        } else if (!elementID.equals(other.elementID)) {
            return false;
        }
        return true;
    }
}
