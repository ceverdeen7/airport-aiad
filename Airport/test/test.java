package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import problems.AircraftProblem;
import problems.CrewProblem;
import problems.Event;
import problems.PaxProblem;
import problems.Problem;
import problems.Warning;

import airline.Aircraft;
import airline.AircraftModel;
import airline.Airport;
import airline.CrewMember;
import airline.EscCrew;
import airline.Flight;
import airline.Rank;

public class test {
	Map<String, Aircraft> aircrft;
	Map<String, Rank> rank;
	ArrayList<CrewMember> crewmember;
	Map<String, Airport> airport;
	Map<String, AircraftModel> airModel;
	ArrayList<Flight> flight;
	ArrayList<EscCrew> escCrews;
	ArrayList<Event> events;

	Map<String, Problem> problems;


	public Problem analiseEvents() {
		String type;
		int delay;
		String description;

		int warningAircraft = 50;
		int warningCrew = 25;
		int warningPax = 35;

		Warning warning;
		CrewProblem crewProblem;
		AircraftProblem airProblem;
		PaxProblem paxProblem;
		Problem problem=null;
		List<Warning> warnings = new ArrayList<Warning>();
		List<Problem> problems = new ArrayList<Problem>();

		for (int i = 0; i != events.size(); i++) {
			warning = null;
			crewProblem = null;
			airProblem = null;
			
			paxProblem = null;

			// criar delay na an�lise de eventos
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// ler dados evento
			type = events.get(i).getType();
			delay = events.get(i).getDelay();
			description = events.get(i).getDescription();

			if (type.equalsIgnoreCase("aircraft")) {
				if (delay < warningAircraft) {
					warning = new Warning(type, description, delay);
				} else {
					problem = new Problem();
					airProblem = new AircraftProblem(description, delay);
					problem.addAirProbs(airProblem);
				}

			} else if (type.equalsIgnoreCase("crewmember")) {
				if (delay < warningCrew) {
					warning = new Warning(type, description, delay);
				} else {
					problem = new Problem();
					crewProblem = new CrewProblem(description, delay);
					problem.addCrewProbs(crewProblem);
				}

			} else {
				if (delay < warningPax) {
					warning = new Warning(type, description, delay);
				} else {
					problem = new Problem();
					paxProblem = new PaxProblem(description, delay);
					problem.addPaxProbs(paxProblem);
				}
			}
			if (warning != null)
			{
				warnings.add(warning);
				warning.print();
				
			}
			else if (problem!= null)
			{
				problems.add(problem);
				problem.print();
				
			}
		}
		return problem;
	}

	private void parseEvents() {
		ParseExcel parExc = new ParseExcel();
		parExc.openFile("EVENTS.xls");

		events = parExc.getEvents(parExc.getFile().getSheet(0), flight,
				escCrews);

		parExc.closeFile();
	}

	public void parseFlights() {
		// Ler Planeamento.
		ParseExcel parExc = new ParseExcel();
		parExc.openFile("FLIGHTS_2009_09.xls");
		airModel = parExc.getAircraftModels(parExc.getFile().getSheet(2));
		airport = parExc.getAirports(parExc.getFile().getSheet(3));
		aircrft = parExc.getAircrafts(parExc.getFile().getSheet(1), airModel);
		rank = parExc.getRanks(parExc.getFile().getSheet(5), airModel);
		crewmember = parExc.getCrewMembers(parExc.getFile().getSheet(4), rank);

		flight = parExc.getFlights(parExc.getFile().getSheet(0), airport,
				aircrft);
		escCrews = parExc.getEscCrews(flight, crewmember, rank);
		parExc.closeFile();

		for (int i = 0; i != escCrews.size(); i++) {
			escCrews.get(i).print();
			System.out.println("----------------------------------------");
		}
		System.out.println("----------------------------------------");
	}
}
