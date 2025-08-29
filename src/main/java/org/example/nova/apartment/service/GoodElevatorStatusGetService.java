package org.example.nova.apartment.service;

import org.example.nova.apartment.domain.Elevator;
import org.example.nova.apartment.dto.ElevatorStatusGetRequestToApartmentServer;
import org.example.nova.apartment.dto.ElevatorStatusResponseFromApartmentServer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.example.nova.apartment.domain.ElevatorDoorStatus.CLOSE;
import static org.example.nova.apartment.domain.ElevatorDoorStatus.OPEN;
import static org.example.nova.apartment.domain.ElevatorStatus.*;

@Service
public class GoodElevatorStatusGetService implements ElevatorStatusGetService {

    private final Map<ElevatorKey, Elevator> elevatorRepository;

    public GoodElevatorStatusGetService() {
        this.elevatorRepository = new HashMap<>();
        this.elevatorRepository.put(new ElevatorKey("101", "1"), new Elevator(NORMAL, OPEN, 1));
        this.elevatorRepository.put(new ElevatorKey("101", "2"), new Elevator(NORMAL, CLOSE, 2));
        this.elevatorRepository.put(new ElevatorKey("101", "3"), new Elevator(FAULT, CLOSE, 1));
        this.elevatorRepository.put(new ElevatorKey("102", "1"), new Elevator(FULL, CLOSE, 10));
    }

    @Override
    public ElevatorStatusResponseFromApartmentServer getElevatorStatus(ElevatorStatusGetRequestToApartmentServer request) {
        ElevatorKey elevatorKey = new ElevatorKey(request.getDong(), request.getHogi());
        if (!elevatorRepository.containsKey(elevatorKey)) {
            throw new NoSuchElementException("요청한 엘리베이터가 존재하지 않습니다.");
        }

        Elevator elevator = elevatorRepository.get(elevatorKey);
        return new ElevatorStatusResponseFromApartmentServer(
                elevator.getElevatorStatus().getCode(),
                elevator.getDoorStatus().name(),//dd
                String.valueOf(elevator.getCurrentFloor())
        );
    }

    public record ElevatorKey(String dong, String hogi) {}
}
