package com.jtbdevelopment.TwistedWordSearch.rest.services;

import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo;
import com.jtbdevelopment.TwistedWordSearch.rest.data.GameFeatureInfo.Detail;
import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame;
import com.jtbdevelopment.games.mongo.players.MongoPlayer;
import com.jtbdevelopment.games.rest.services.AbstractPlayerGatewayService;
import com.jtbdevelopment.games.rest.services.AbstractPlayerServices;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * Date: 11/14/14 Time: 6:36 AM
 */
@Path("/")
@Component
public class TWSPlayerGatewayService extends
    AbstractPlayerGatewayService<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> {

  public TWSPlayerGatewayService(
      final AbstractPlayerServices<ObjectId, GameFeature, TWSGame, MaskedGame, MongoPlayer> playerServices) {
    super(playerServices);
  }

  @GET
  @Path("features")
  @Produces(MediaType.APPLICATION_JSON)
  public List<GameFeatureInfo> featuresAndDescriptions() {
    Map<GameFeature, List<GameFeature>> groupedFeatures = GameFeature.getGroupedFeatures();
    return groupedFeatures.keySet().stream()
        .sorted(Comparator.comparingInt(GameFeature::getOrder))
        .map(group -> new GameFeatureInfo(
            group,
            groupedFeatures.get(group).stream().map(Detail::new).collect(Collectors.toList())))
        .collect(Collectors.toList());
  }

}
