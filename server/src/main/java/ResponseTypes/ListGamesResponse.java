package ResponseTypes;

import model.GameData;

import java.util.HashSet;

public record ListGamesResponse (HashSet<GameData> games){
}
