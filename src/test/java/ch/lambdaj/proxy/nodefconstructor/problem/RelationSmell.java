package ch.lambdaj.proxy.nodefconstructor.problem;


public class RelationSmell {
    private final ElementId from;
    private final ElementId to;

    public RelationSmell(final ElementId from, final ElementId to) {
        super();
        this.from = from;
        this.to = to;
    }

    public ElementId getFrom() {
        return from;
    }

    public ElementId getTo() {
        return to;
    }

    public boolean isForRelation(final ElementId from, final ElementId to) {
        return this.from.equals(from) && this.to.equals(to);
    }
}
