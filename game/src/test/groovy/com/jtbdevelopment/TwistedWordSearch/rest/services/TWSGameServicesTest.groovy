package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.handlers.SubmitFindHandler
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.TwistedWordSearch.state.masking.MaskedGame
import com.jtbdevelopment.games.mongo.MongoGameCoreTestCase
import groovy.transform.TypeChecked
import org.bson.types.ObjectId

import javax.ws.rs.Consumes
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 9/3/2016
 * Time: 6:58 PM
 */
class TWSGameServicesTest extends MongoGameCoreTestCase {
    TWSGameServices services = new TWSGameServices()

    void testActionAnnotations() {
        Map<String, List<Object>> stuff = [
                //  method: [name, params, path, path param values, consumes
                "findWord"   : ["find", [List.class], [], [MediaType.APPLICATION_JSON]],
        ]
        stuff.each {
            String method, List<Object> details ->
                def m = TWSGameServices.getMethod(method, details[1] as Class[])
                int expectedA = 3 + details[3].size
                assert (m.annotations.size() == expectedA ||
                        (m.annotations.size() == (expectedA + 1) && m.isAnnotationPresent(TypeChecked.TypeCheckingInfo.class))
                )
                assert m.isAnnotationPresent(PUT.class)
                assert m.isAnnotationPresent(Produces.class)
                assert m.getAnnotation(Produces.class).value() == [MediaType.APPLICATION_JSON]
                assert m.isAnnotationPresent(Path.class)
                assert m.getAnnotation(Path.class).value() == details[0]
                if (details[3].size > 0) {
                    assert m.isAnnotationPresent(Consumes.class)
                    assert m.getAnnotation(Consumes.class).value() == details[3]
                }
                if (details[2].size > 0) {
                    int count = 0
                    details[2].each {
                        String pp ->
                            ((PathParam) m.parameterAnnotations[count][0]).value() == pp
                            ++count
                    }
                }
        }
    }

    void testFindWordUsesHandler() {
        MaskedGame maskedGame = new MaskedGame()
        List<GridCoordinate> inputCoordinates = [new GridCoordinate(0, 0), new GridCoordinate(1,1), new GridCoordinate(1,1)]
        ObjectId gameId = new ObjectId()
        services.playerID.set(PONE.id)
        services.gameID.set(gameId)
        services.submitFindHandler = [
                handleAction: {
                    Serializable p, Serializable g, List<GridCoordinate> t ->
                        assert PONE.id.is(p)
                        assert gameId.is(g)
                        assert inputCoordinates.is(t)
                        maskedGame
                }
        ] as SubmitFindHandler
        assert maskedGame.is(services.findWord(inputCoordinates))

    }
}
