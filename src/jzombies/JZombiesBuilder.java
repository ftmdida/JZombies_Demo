package jzombies;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import jzombies.util.DefenderParser;
import jzombies.util.UserAndAttackerParser;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class JZombiesBuilder implements ContextBuilder<Object> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context
	 * .Context)
	 */
	@Override
	public Context build(Context<Object> context) {
		context.setId("jzombies");

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>(
				"infection network", context, false);
		netBuilder.buildNetwork();
		
		
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
				"space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(), 50,
				50);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, 50, 50));
		
		Network<Object> net = (Network<Object>)context.getProjection("infection network"); 
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		
		DefenderParser def= new DefenderParser();
		Defender defender;
		
		try {
			defender = def.parseDefender(space, grid);
			context.add(defender);
			
			UserAndAttackerParser users= new UserAndAttackerParser();
			ArrayList<Agent> agentsList = new ArrayList<Agent>();
			try {
				agentsList=users.parseUsers(space, grid);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
			
			for (Agent agent : agentsList) {
				if(agent instanceof User){
						net.addEdge(agent, defender);
						agent.connect(defender);
						context.add(agent);			
				}
				else {
						net.addEdge(agent,defender);
						agent.connect(defender);
						context.add(agent);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e1) {
			e1.printStackTrace();
		}
		

		

		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(20);
		}

		return context;
	}
}
