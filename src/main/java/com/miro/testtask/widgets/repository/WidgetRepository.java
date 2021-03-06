package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.WidgetFilter;
import com.miro.testtask.widgets.model.WidgetModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface WidgetRepository {
  List<WidgetModel> findAll();

  Page<WidgetModel> findPaged(Pageable pageable);

  Page<WidgetModel> findFilteredPaged(Pageable pageable, @Nullable WidgetFilter filter);

  Optional<WidgetModel> findByZindex(int zindex);

  WidgetModel create(WidgetModel widgetModel);

  Optional<WidgetModel> findById(long id);

  WidgetModel update(WidgetModel widgetModel);


  int maximumZindex();

  void delete(WidgetModel widgetModel);
}
