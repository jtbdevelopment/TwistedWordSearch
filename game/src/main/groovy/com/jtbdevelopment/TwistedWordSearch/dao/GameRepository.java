package com.jtbdevelopment.TwistedWordSearch.dao;

import com.jtbdevelopment.TwistedWordSearch.state.GameFeature;
import com.jtbdevelopment.TwistedWordSearch.state.TWSGame;
import com.jtbdevelopment.games.mongo.dao.AbstractMongoMultiPlayerGameRepository;

/**
 * Date: 7/13/16 Time: 7:13 PM
 */
public interface GameRepository extends
    AbstractMongoMultiPlayerGameRepository<GameFeature, TWSGame> {

}
