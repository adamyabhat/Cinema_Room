package cinema.controllers;

import cinema.models.ReturnToken;
import cinema.models.Seat;
import cinema.models.SeatToBook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class SeatsController {
    private List<Seat> Seats = new ArrayList<>();
    private Map <UUID, Seat> Tokens = new HashMap<>();
    SeatsController() {
        for (int i = 1 ; i <= 9; i++) {
            for (int j = 1 ; j <= 9; j++) {
                if (i <= 4) {
                    Seats.add(new Seat(i, j, 10));
                } else {
                    Seats.add(new Seat(i, j, 8));
                }
            }
        }
    }

    public List<Seat> getAvilableSeats() {
        List<Seat> AvilableSeats = new ArrayList<>();
        for (Seat Seat : Seats ) {
            if (!Seat.isBooked()){
                AvilableSeats.add(Seat);
            }
        }
        return  AvilableSeats;
    }

    @GetMapping("/seats")
    public Object getSeats(){

        return Map.of("total_rows",9,
                "total_columns", 9,
                "available_seats", getAvilableSeats());
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSeats(@RequestBody SeatToBook SeatRequested){
        for (Seat Seat: Seats) {
            if (Seat.getRow() == SeatRequested.getRow() &&
                    Seat.getColumn() == SeatRequested.getColumn()) {
                if (!Seat.isBooked()) {
                    Seat.setIsBooked(true);
                    Map Resp = Map.of("row", Seat.getRow(),
                            "column", Seat.getColumn(),
                            "price", Seat.getPrice());
                    UUID Token = UUID.randomUUID();
                    Tokens.put(Token,Seat);
                    Map RespBody = Map.of("token",Token,"ticket",Resp);
                    return new ResponseEntity<>(RespBody, HttpStatus.OK);
                } else {
                    return new ResponseEntity(Map.of("error",
                            "The ticket has been already purchased!"),
                            HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity(Map.of("error", "The number of a row or a column is out of bounds!"),
                HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody ReturnToken Ticket){
        try {
            Seat SeatBooked = Tokens.get(Ticket.getToken());
            SeatBooked.setIsBooked(false);
            Tokens.remove(Ticket.getToken());
            return new ResponseEntity<>(Map.of("returned_ticket", SeatBooked),HttpStatus.OK);
        } catch (Exception E){
            return new ResponseEntity<>(Map.of("error","Wrong token!"),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(value = "password", required = false) String Password){
        if (Password != null && Password.equals("super_secret")) {
            int NumberOfAvilableSeats = getAvilableSeats().size();
            int NumberOfPurchasedSeats = Seats.size() - NumberOfAvilableSeats;
            int CurrentIncome = 0;
            for (Seat Seat : Seats) {
                if (Seat.isBooked()){
                    CurrentIncome = CurrentIncome + Seat.getPrice();
                }
            }
            return new ResponseEntity<>(Map.of("current_income",CurrentIncome,
                    "number_of_available_seats",NumberOfAvilableSeats,
                    "number_of_purchased_tickets",NumberOfPurchasedSeats),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error", "The password is wrong!"),HttpStatus.UNAUTHORIZED);
        }
    }

}
