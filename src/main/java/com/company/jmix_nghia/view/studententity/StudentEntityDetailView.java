package com.company.jmix_nghia.view.studententity;

import com.company.jmix_nghia.entity.StudentEntity;
import com.company.jmix_nghia.entity.SubjectEntity;
import com.company.jmix_nghia.view.main.MainView;
import com.company.jmix_nghia.view.subjectentity.SubjectEntityDetailView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.exception.ValidationException;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "studentEntities/:id", layout = MainView.class)
@ViewController(id = "StudentEntity.detail")
@ViewDescriptor(path = "student-entity-detail-view.xml")
@EditedEntityContainer("studentEntityDc")
public class StudentEntityDetailView extends StandardDetailView<StudentEntity> {

    @ViewComponent
    private CollectionPropertyContainer<SubjectEntity> subjectEntitiesDc;
    @ViewComponent
    private InstanceLoader<SubjectEntity> subjectEntityDc;
    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private DataGrid<SubjectEntity> subjectEntitiesDataGrid;

    @Subscribe("subjectEntitiesDataGrid.create")
    public void onSubjectEntitiesDataGridCreate(final ActionPerformedEvent event) {
        SubjectEntity subject = subjectEntitiesDataGrid.getSingleSelectedItem();

        DialogWindow<SubjectEntityDetailView> window =
        // Xây dựng dialog và mở view
        dialogWindows.detail(this, SubjectEntity.class)
                .withViewClass(SubjectEntityDetailView.class)
                .newEntity(subject)
                .build();

        window.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.SAVE)) {
                subjectEntitiesDc.getMutableItems().add(window.getView().getEditedEntity());
            }

        });
        window.open();

    }

    @Subscribe("subjectEntitiesDataGrid.edit")
    public void onSubjectEntitiesDataGridEdit(final ActionPerformedEvent event) {
        SubjectEntity subject = subjectEntitiesDataGrid.getSingleSelectedItem();

        DialogWindow<SubjectEntityDetailView> window =
        // Xây dựng dialog và mở view
        dialogWindows.detail(this, SubjectEntity.class)
                .withViewClass(SubjectEntityDetailView.class)
                .editEntity(subject)
                .build();

        window.addAfterCloseListener(afterCloseEvent -> {
            if (afterCloseEvent.closedWith(StandardOutcome.SAVE)) {
//                colCOOwnerListDc.getMutableItems().forEach(colCOOwnerList -> {
//                    getEditedEntity().getColCOOwnerList().stream().filter(
//                            coOwnerList -> coOwnerList.getCOCusCode().getCusCode().equals(window.getView().getEditedEntity().getCOCusCode().getCusCode()) && !coOwnerList.equals(window.getView().getEditedEntity())
//                    ).findFirst().ifPresent(colTransactionDetail -> {
//                        throw new ValidationException(String.format(messages.getMessage(CONST.ERROR_GROUP, "0114000014"), window.getView().getEditedEntity().getCOCusCode().getCusCode()));
//                    });
//                });
                subjectEntitiesDc.getMutableItems().remove(subjectEntitiesDataGrid.getSingleSelectedItem());
                subjectEntitiesDc.getMutableItems().add(window.getView().getEditedEntity());
            }

        });
        window.open();
    }
}