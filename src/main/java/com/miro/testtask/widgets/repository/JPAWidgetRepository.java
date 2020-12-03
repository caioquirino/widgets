package com.miro.testtask.widgets.repository;

import com.miro.testtask.widgets.model.WidgetFilter;
import com.miro.testtask.widgets.model.WidgetModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.ref.Cleaner;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@ConditionalOnSingleCandidate(DataSource.class)
public interface JPAWidgetRepository extends WidgetRepository, JpaRepository<WidgetModel, Long>, Cleaner.Cleanable {

  @Query("SELECT w from WidgetModel as w order by w.zindex asc")
  List<WidgetModel> findAll();

  @Query("SELECT w from WidgetModel as w order by w.zindex asc")
  Page<WidgetModel> findAll(Pageable pageable);

  default Page<WidgetModel> FindAll(Pageable pageable, @Nullable WidgetFilter filter) {
    if(filter == null) {
      return findAll(pageable);
    } else {
      return findAll(filter.getX(), filter.getY(), filter.getWidth(), filter.getHeight(), pageable);
    }
  }


  @Query("SELECT w FROM WidgetModel as w WHERE w.coordinate.x >= :x AND w.coordinate.y >= :y AND w.width <= :width + :x AND w.height<= :height + :y order by w.zindex asc")
  Page<WidgetModel> findAll(
      @Param("x") int x,
      @Param("y") int y,
      @Param("width") int width,
      @Param("height") int height,
      Pageable pageable);


  Optional<WidgetModel> findByZindex(int zindex);

  default WidgetModel create(WidgetModel widgetModel) {
    return save(widgetModel);
  }


  default WidgetModel update(WidgetModel widgetModel) {
    return save(widgetModel);
  }

  Optional<WidgetModel> findById(long id);

  @Query("select max(w.zindex) from WidgetModel as w")
  int maximumZindex();

  @Modifying
  @Query("delete from WidgetModel")
  @Transactional
  void clean();
}
