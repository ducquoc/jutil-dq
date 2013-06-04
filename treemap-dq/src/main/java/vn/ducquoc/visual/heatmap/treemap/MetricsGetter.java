package vn.ducquoc.visual.heatmap.treemap;

public interface MetricsGetter<T> {

    Double getSizeValue(T object);

    Double getDensityValue(T object);

}
