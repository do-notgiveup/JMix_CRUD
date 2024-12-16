package com.company.jmix_nghia.view.subjectentity;

import com.company.jmix_nghia.entity.SubjectEntity;
import com.company.jmix_nghia.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "subjectEntities", layout = MainView.class)
@ViewController(id = "SubjectEntity.list")
@ViewDescriptor(path = "subject-entity-list-view.xml")
@LookupComponent("subjectEntitiesDataGrid")
@DialogMode(width = "64em")
public class SubjectEntityListView extends StandardListView<SubjectEntity> {
}