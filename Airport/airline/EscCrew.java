package airline;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EscCrew {
	private List<CrewMember> crewMembers;
	private List<Flight> flights;
	private Timestamp startTime;
	private Timestamp endTime;
	private Airport lastAirport;
	private Long workTime;

	public EscCrew(List<CrewMember> crewMembers, Flight flight) {
		super();
		this.crewMembers = crewMembers;
		this.flights = new ArrayList<Flight>();
		this.flights.add(flight);
		this.startTime = flight.getDepartureTime();
		this.endTime = flight.getArrivalTime();
		this.lastAirport = flight.getArrivalAirport();
		this.workTime = 0L;
	}

	public Airport getLastAirport() {
		return lastAirport;
	}

	public void setLastAirport(Airport lastAirport) {
		this.lastAirport = lastAirport;
	}

	public void addFlight(Flight flight) {
		this.flights.add(flight);
		endTime = flight.getArrivalTime();
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public List<CrewMember> getCrewMembers() {
		return crewMembers;
	}

	public void setCrewMembers(List<CrewMember> crewMembers) {
		this.crewMembers = crewMembers;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public boolean isWorkTimeLimitReached() {
		// 3 days = 86400000 ms
		if (workTime > 86400000)
			return true;
		else
			return false;

	}

	public Long getWorkTime() {
		return workTime;
	}

	public void setWorkTime(Long workTime) {
		this.workTime = workTime;
	}

	public void print() {
		for (int i = 0; i != crewMembers.size(); i++)
			crewMembers.get(i).print();
		for (int i = 0; i != flights.size(); i++)
			flights.get(i).print();
		System.out.println("work Time: " + workTime);
	}

	public void addDelay(int k, Long delay) {
		for (int i = k; i != flights.size(); i++) {
			flights.get(i).setDepartureTime(
					new Timestamp(flights.get(i).getDepartureTime().getTime()
							+ delay));
			flights.get(i).setArrivalTime(
					new Timestamp(flights.get(i).getArrivalTime().getTime()
							+ delay));
		}

	}

	public int getHowManyFlightsLeft(int k) {
		return flights.size() - k;

	}

	public Airport getCurrentLocation(Timestamp departureTime) {
		for (int i = 0; i != flights.size(); i++) {
			// <0 argumento � depois
			if (i == 0
					&& flights.get(i).getDepartureTime().compareTo(
							departureTime) < 0) {
				return flights.get(i).getDepartureAirport();
			} else if (flights.get(i).getDepartureTime().compareTo(
					departureTime) < 0
					&& flights.get(i - 1).getArrivalTime().compareTo(
							departureTime) > 0) {
				return flights.get(i).getDepartureAirport();
			}

		}
		return null;
	}

	public boolean getDisponibilityToFly(Timestamp departureTime,
			Long tripDuration, Airport airport) {
		Long avaiableTime = 0L;
		for (int i = 0; i != flights.size(); i++) {
			// <0 argumento � depois
			if (flights.get(i).getDepartureAirport() == airport) {
				if (i == 0
						&& flights.get(i).getDepartureTime().compareTo(
								departureTime) > 0) {

					avaiableTime = flights.get(i).getDepartureTime().getTime()
							- departureTime.getTime();

					if (avaiableTime > 2 * tripDuration)
						return true;

				} else if (i != 0
						&& flights.get(i).getDepartureTime().compareTo(
								departureTime) > 0
						&& flights.get(i - 1).getArrivalTime().compareTo(
								departureTime) < 0) {

					avaiableTime = flights.get(i - 1).getArrivalTime()
							.getTime()
							- flights.get(i).getDepartureTime().getTime();

					if (avaiableTime > 2 * tripDuration)
						return true;
				}
			}

		}
		return false;
	}

	public int getPaxAffected(int k) {
		int paxAffected = 0;
		for (int i = k; i != flights.size(); i++) {
			paxAffected += flights.get(i).getBusActlSeats()
					+ flights.get(i).getEconActlSeats();
		}
		return paxAffected;
	}

	public CrewMember findFirstCrewMemberByRank(Rank rank) {
		for (int i = 0; i != crewMembers.size(); i++) {
			if (crewMembers.get(i).getRank() == rank)
				return crewMembers.get(i);
		}
		return null;
	}

	public CrewMember getDisponibilityToCrewMemberFly(Timestamp departureTime,
			int delay, Airport airport, CrewMember oldCrewMember) {

		Rank rank = oldCrewMember.getRank();

		Timestamp arrivalTimeOfCrewMember = new Timestamp(departureTime.getTime()+delay);
		
		
		for (int i = 0; i != flights.size(); i++) {
			// <0 argumento � depois
			if (flights.get(i).getDepartureAirport() == airport) {

				if (i == 0
						&& flights.get(i).getDepartureTime().compareTo(
								arrivalTimeOfCrewMember) > 0) {
					return findFirstCrewMemberByRank(rank);

				} else if (i != 0
						&& flights.get(i).getDepartureTime().compareTo(
								arrivalTimeOfCrewMember) > 0
						&& flights.get(i - 1).getArrivalTime().compareTo(
								departureTime) < 0) {
					return findFirstCrewMemberByRank(rank);

				}
			}

		}
		// se os aeroporto do parametro for igual ao aeroporto de partida na
		// data coiso

		// se departure time + delay for inferior � partida do voo em questao

		// entao se os ranks forem iguais vai ser possivel trocar um crewMember
		// deste voo
		// pelo atrasadinho

		// TODO Auto-generated method stub
		return null;
	}

}
