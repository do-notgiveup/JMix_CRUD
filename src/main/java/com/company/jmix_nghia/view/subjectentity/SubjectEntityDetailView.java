package com.company.jmix_nghia.view.subjectentity;

import com.company.jmix_nghia.entity.SubjectEntity;
import com.company.jmix_nghia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "subjectEntities/:id", layout = MainView.class)
@ViewController(id = "SubjectEntity.detail")
@ViewDescriptor(path = "subject-entity-detail-view.xml")
@EditedEntityContainer("subjectEntityDc")
public class SubjectEntityDetailView extends StandardDetailView<SubjectEntity> {
}