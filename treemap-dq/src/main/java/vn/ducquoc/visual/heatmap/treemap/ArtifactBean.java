package vn.ducquoc.visual.heatmap.treemap;

public class ArtifactBean {

    private String name;
    private Double cost;
    private String healthColor;

    public ArtifactBean(String name, Double cost, String healthColor) {
        this.name = name;
        this.cost = cost;
        this.healthColor = healthColor;
    }

    //
    // trivial setters/getters
    //
    public String getName() {
        return name;
    }

    public Double getCost() {
        return cost;
    }

    public String getHealthColor() {
        return healthColor;
    }

    // for fluent interface (method chaining)
    public ArtifactBean setHealthColor(String healthColor) {
        this.healthColor = healthColor;
        return this;
    }

    public ArtifactBean setCost(Double cost) {
        this.cost = cost;
        return this;
    }

    public ArtifactBean setName(String name) {
        this.name = name;
        return this;
    }

}
