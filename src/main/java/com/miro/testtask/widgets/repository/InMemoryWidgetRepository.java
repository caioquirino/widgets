package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.WidgetModel;
import org.springframework.stereotype.Repository;

import java.lang.ref.Cleaner;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class InMemoryWidgetRepository implements WidgetRepository, Cleaner.Cleanable {

  private final Map<Long, WidgetModel> widgetModelMap;
  private long lastId;

  public InMemoryWidgetRepository(List<WidgetModel> widgetModelList) {
    this.lastId = widgetModelList.stream().map(WidgetModel::getId).max(Long::compareTo).orElse(0L);

    this.widgetModelMap = widgetModelList.stream().collect(Collectors.toMap(WidgetModel::getId, Function.identity(),
        (previousWidget, newWidget) -> newWidget, TreeMap::new));
  }

  public InMemoryWidgetRepository() {
    this(List.of());
  }


  @Override
  public void clean() {
    this.widgetModelMap.clear();
    this.lastId = 0;
  }

  @Override
  public List<WidgetModel> findAll() {
    return List.copyOf(this.widgetModelMap.values());
  }

  @Override
  public WidgetModel create(WidgetModel widgetModel) {
    var newModel = widgetModel.toBuilder().id(++lastId).build();
    this.widgetModelMap.put(newModel.getId(), newModel);
    return newModel;
  }

  @Override
  public WidgetModel update(WidgetModel widgetModel) {
    this.widgetModelMap.put(widgetModel.getId(), widgetModel);

    return widgetModel;
  }

  @Override
  public int maximumzindex() {
    return this.widgetModelMap.values().stream().map(WidgetModel::getZindex).max(Integer::compareTo).orElse(0);
  }

  @Override
  public void delete(WidgetModel widgetModel) {
    this.widgetModelMap.remove(widgetModel.getId());
  }
}
