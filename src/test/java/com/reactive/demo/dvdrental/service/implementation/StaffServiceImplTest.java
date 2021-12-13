package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.data.repository.StaffRepository;
import com.reactive.demo.dvdrental.exception.DataNotFoundException;
import com.reactive.demo.dvdrental.service.implementation.StaffServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class StaffServiceImplTest {

    @InjectMocks
    private StaffServiceImpl staffService;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private GenericMapper genericMapper;

    private Staff staffSample = Staff.builder().staffId(1).firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();
    private StaffCoreModel staffCoreModel = StaffCoreModel.builder().firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();

    @Test
    @DisplayName("Get All Staff members")
    public void testFindAll_Successful() {
        BDDMockito.when(staffRepository.findAll())
                .thenReturn(Flux.just(staffSample));
        StepVerifier.create(staffService.findAll())
                .expectSubscription()
                .expectNext(staffSample)
                .verifyComplete();

    }

    @Test
    @DisplayName("Get All Staff members returns empty")
    public void testFindAll_NoDataFound() {
        BDDMockito.when(staffRepository.findAll())
                .thenReturn(Flux.empty());
        StepVerifier.create(staffService.findAll())
                .expectSubscription()
                .expectError(DataNotFoundException.class)
                .verify();

    }

    @Test
    @DisplayName("Get by Id when there is data")
    public void testGetById_Successful() {
        BDDMockito.when(staffRepository.findFirstByStaffId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.just(staffSample));
        StepVerifier.create(staffService.findById(1L))
                .expectSubscription()
                .expectNext(staffSample)
                .verifyComplete();

    }

    @Test
    @DisplayName("Get by Id when there is no data")
    public void testGetById_WhenEmpty() {
        BDDMockito.when(staffRepository.findFirstByStaffId(ArgumentMatchers.anyLong()))
                .thenReturn(Flux.empty());
        StepVerifier.create(staffService.findById(1L))
                .expectSubscription()
                .expectError(DataNotFoundException.class)
                .verify();

    }

    @Test
    @DisplayName("Create new staff resource")
    public void testCreateStaff_Successful() {
        BDDMockito.when(staffRepository.findFirstByFirstNameAndLastNameAndEmailAndUsername(ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
        BDDMockito.when(genericMapper.convertToStaff(ArgumentMatchers.any(StaffCoreModel.class)))
                .thenReturn(staffSample);
        BDDMockito.when(staffRepository.save(ArgumentMatchers.any(Staff.class)))
                .thenReturn(Mono.just(staffSample));

        StepVerifier.create(staffService.save(staffCoreModel))
                .expectSubscription()
                .expectNext(Pair.of(false, staffSample))
                .verifyComplete();

    }

    @Test
    @DisplayName("Create returns existing staff resource")
    public void testCreateStaff_SuccessfulAlreadyExists() {
        BDDMockito.when(staffRepository.findFirstByFirstNameAndLastNameAndEmailAndUsername(ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(staffSample));
        BDDMockito.when(genericMapper.convertToStaff(ArgumentMatchers.any(StaffCoreModel.class)))
                .thenReturn(staffSample);
        BDDMockito.verify(staffRepository, BDDMockito.times(0)).save(ArgumentMatchers.any(Staff.class));

        StepVerifier.create(staffService.save(staffCoreModel))
                .expectSubscription()
                .expectNext(Pair.of(true, staffSample))
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete by Id when there is data")
    public void testDeleteById_Successful() {
        BDDMockito.when(staffRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(staffSample));
        BDDMockito.when(staffRepository.delete(ArgumentMatchers.any(Staff.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(staffService.delete(1L))
                .expectSubscription()
                .expectNext(Boolean.TRUE)
                .verifyComplete();

    }

    @Test
    @DisplayName("Delete by Id when there is no data")
    public void testDeleteById_WhenEmpty() {
        BDDMockito.when(staffRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(staffService.delete(1L))
                .expectSubscription()
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        BDDMockito.verify(staffRepository, BDDMockito.times(0)).delete(ArgumentMatchers.any(Staff.class));
    }
}