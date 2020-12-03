package com.miro.testtask.widgets.service;


import com.miro.testtask.widgets.model.WidgetFilter;
import com.miro.testtask.widgets.model.WidgetModel;
import com.miro.testtask.widgets.repository.WidgetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Service
public class WidgetService {

  private final WidgetRepository repository;

  public WidgetService(WidgetRepository repository) {
    this.repository = repository;
  }

  public List<WidgetModel> findAll() {
    return repository.findAll();
  }

  public Page<WidgetModel> findPaged(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Page<WidgetModel> findFilteredPaged(Pageable pageable, @Nullable WidgetFilter filter) {
    return repository.FindAll(pageable, filter);
  }

  @Transactional
  public WidgetModel create(WidgetModel widgetModel) {
    WidgetModel newWidgetModel = widgetModel;

    if (widgetModel.getZindex() == null) {
      newWidgetModel = newWidgetModel.toBuilder().zindex(this.repository.maximumZindex() + 1).build();
    } else {
      fixZindex(newWidgetModel);
    }

    return repository.create(newWidgetModel);
  }

  @Transactional
  public WidgetModel update(WidgetModel widgetModel) {
    fixZindex(widgetModel);
    return repository.update(widgetModel);
  }

  private void fixZindex(WidgetModel widgetModel) {
    Deque<WidgetModel> widgetsToBeChanged = new LinkedList<>();

    var lastZindex = widgetModel.getZindex();

    while (true) {
      var persistedWithSameZindex = this.repository.findByZindex(lastZindex);

      if (persistedWithSameZindex.isEmpty()) {
        break;
      }

      widgetsToBeChanged.addFirst(persistedWithSameZindex.get().toBuilder().zindex(lastZindex + 1).build());
      lastZindex++;
    }

    widgetsToBeChanged.forEach(this.repository::update);
  }


  @Transactional
  public void delete(WidgetModel widgetModel) {
    repository.delete(widgetModel);
  }

}
