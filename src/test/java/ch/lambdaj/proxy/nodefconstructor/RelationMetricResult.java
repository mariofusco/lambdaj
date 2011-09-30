package ch.lambdaj.proxy.nodefconstructor;



public class RelationMetricResult {
    private final ElementId from;
    private final ElementId to;
    private final double metric;
    
    //protected RelationMetricResult() {}

    public RelationMetricResult(ElementId from, ElementId to, double metric) {
        this.from = from;
        this.to = to;
        this.metric = metric;
    }

    public ElementId from() {
        return this.from;
    }
    
    public ElementId to() {
        return this.to;
    }

    public double getMetricValue() {
        return this.metric;
    }

    @Override
    public String toString() {
        return String.valueOf(getMetricValue());
    }

    public boolean isForRelation(ElementId from, ElementId to) {
        return this.from.equals(from) && this.to.equals(to);
    }

    public Integer intFrom() {
        return from().intValue();
    }
}
