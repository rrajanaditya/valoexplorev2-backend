package com.valoexplore.backend.service;

import com.valoexplore.backend.model.*;
import com.valoexplore.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final RestTemplate restTemplate;
    private final SkinRepository skinRepository;
    private final PlayerCardRepository cardRepository;
    private final CompetitiveRankRepository rankRepository;
    private final BuddyRepository buddyRepository;
    private final SprayRepository sprayRepository;
    private final AgentRepository agentRepository;

    private static final String API_BASE = "https://valorant-api.com/v1";
    private final Random random = new Random();

    @Override
    public void seedAllAssets() {
        System.out.println(">>> Starting Asset Ingestion...");
        seedRanksIfEmpty();
        seedSkinsIfEmpty();
        seedCardsIfEmpty();
        seedBuddiesIfEmpty();
        seedSpraysIfEmpty();
        seedAgentsIfEmpty();
        System.out.println(">>> Asset Ingestion Complete.");
    }

    private void seedSkinsIfEmpty() {
        if (skinRepository.count() > 0) return;

        Map<String, String> tierMap = fetchContentTiers();
        String url = API_BASE + "/weapons/skins";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() != null && response.getBody().get("data") instanceof List<?> dataList) {
            List<Skin> skins = dataList.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<?, ?>) item)
                    .filter(item -> item.get("displayIcon") != null)
                    .map(item -> {
                        Skin skin = new Skin();
                        String fullName = (String) item.get("displayName");

                        skin.setUuid((String) item.get("uuid"));
                        skin.setDisplayName(fullName);
                        skin.setDisplayIcon((String) item.get("displayIcon"));

                        String tierUuid = (String) item.get("contentTierUuid");
                        skin.setTierName(tierMap.getOrDefault(tierUuid, "Standard"));

                        String weaponType = "Melee";
                        if (fullName != null && fullName.contains(" ")) {
                            weaponType = fullName.substring(fullName.lastIndexOf(" ") + 1);
                        }
                        skin.setWeaponType(weaponType);

                        skin.setPrice(getRandomPrice());
                        skin.setSource(getRandomSource());

                        if (item.get("levels") instanceof List<?> levels) {
                            skin.setTotalLevels(levels.size());
                        } else {
                            skin.setTotalLevels(1);
                        }

                        if (item.get("chromas") instanceof List<?> chromas) {
                            for (Object chromaObj : chromas) {
                                if (chromaObj instanceof Map<?, ?> chroma) {
                                    String variantIcon = (String) chroma.get("displayIcon");
                                    if (variantIcon == null) variantIcon = (String) chroma.get("fullRender");
                                    if (variantIcon != null) skin.getVariants().add(variantIcon);

                                    String swatchIcon = (String) chroma.get("swatch");
                                    if (swatchIcon != null) skin.getSwatches().add(swatchIcon);
                                }
                            }
                        }
                        return skin;
                    }).toList();

            skinRepository.saveAll(skins);
            System.out.println(">>> Seeded " + skins.size() + " Skins.");
        }
    }

    private void seedCardsIfEmpty() {
        if (cardRepository.count() > 0) return;

        String url = API_BASE + "/playercards";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() != null && response.getBody().get("data") instanceof List<?> dataList) {
            List<PlayerCard> cards = dataList.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<?, ?>) item)
                    .filter(item -> item.get("largeArt") != null)
                    .map(item -> {
                        PlayerCard card = new PlayerCard();
                        card.setUuid((String) item.get("uuid"));
                        card.setDisplayName((String) item.get("displayName"));
                        card.setLargeArt((String) item.get("largeArt"));
                        card.setWideArt((String) item.get("wideArt"));
                        card.setPrice(getRandomPrice());
                        return card;
                    }).toList();
            cardRepository.saveAll(cards);
            System.out.println(">>> Seeded " + cards.size() + " Player Cards.");
        }
    }

    private void seedBuddiesIfEmpty() {
        if (buddyRepository.count() > 0) return;

        String url = API_BASE + "/buddies";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() != null && response.getBody().get("data") instanceof List<?> dataList) {
            List<Buddy> buddies = dataList.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<?, ?>) item)
                    .filter(item -> item.get("displayIcon") != null)
                    .map(item -> {
                        Buddy buddy = new Buddy();
                        buddy.setUuid((String) item.get("uuid"));
                        buddy.setDisplayName((String) item.get("displayName"));
                        buddy.setDisplayIcon((String) item.get("displayIcon"));
                        buddy.setPrice(getRandomPrice());
                        return buddy;
                    }).toList();
            buddyRepository.saveAll(buddies);
            System.out.println(">>> Seeded " + buddies.size() + " Buddies.");
        }
    }

    private void seedSpraysIfEmpty() {
        if (sprayRepository.count() > 0) return;

        String url = API_BASE + "/sprays";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() != null && response.getBody().get("data") instanceof List<?> dataList) {
            List<Spray> sprays = dataList.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<?, ?>) item)
                    .filter(item -> item.get("displayIcon") != null)
                    .map(item -> {
                        Spray spray = new Spray();
                        spray.setUuid((String) item.get("uuid"));
                        spray.setDisplayName((String) item.get("displayName"));
                        spray.setDisplayIcon((String) item.get("displayIcon"));
                        spray.setPrice(getRandomPrice());
                        return spray;
                    }).toList();
            sprayRepository.saveAll(sprays);
            System.out.println(">>> Seeded " + sprays.size() + " Sprays.");
        }
    }

    private void seedAgentsIfEmpty() {
        if (agentRepository.count() > 0) return;

        String url = API_BASE + "/agents?isPlayableCharacter=true";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        if (response.getBody() != null && response.getBody().get("data") instanceof List<?> dataList) {
            List<Agent> agents = dataList.stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<?, ?>) item)
                    .filter(item -> item.get("displayIcon") != null)
                    .map(item -> {
                        Agent agent = new Agent();
                        agent.setUuid((String) item.get("uuid"));
                        agent.setDisplayName((String) item.get("displayName"));
                        agent.setDisplayIcon((String) item.get("displayIcon"));
                        agent.setPrice(getRandomPrice());
                        return agent;
                    }).toList();
            agentRepository.saveAll(agents);
            System.out.println(">>> Seeded " + agents.size() + " Agents.");
        }
    }

    private void seedRanksIfEmpty() {
        if (rankRepository.count() > 0) return;

        String url = API_BASE + "/competitivetiers/03621f52-342b-cf4e-4f86-9350a49c6d04";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body != null && body.get("data") instanceof Map<?, ?> dataMap) {
            if (dataMap.get("tiers") instanceof List<?> tiersList) {
                List<CompetitiveRank> ranks = tiersList.stream()
                        .filter(item -> item instanceof Map)
                        .map(item -> (Map<?, ?>) item)
                        .filter(item -> item.get("largeIcon") != null)
                        .map(item -> {
                            CompetitiveRank rank = new CompetitiveRank();
                            rank.setTier((Integer) item.get("tier"));
                            rank.setTierName((String) item.get("tierName"));
                            rank.setLargeIcon((String) item.get("largeIcon"));
                            return rank;
                        }).toList();
                rankRepository.saveAll(ranks);
                System.out.println(">>> Seeded " + ranks.size() + " Competitive Ranks.");
            }
        }
    }

    private Map<String, String> fetchContentTiers() {
        String url = API_BASE + "/contenttiers";
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<>() {}
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !(body.get("data") instanceof List<?> dataList)) {
            return Collections.emptyMap();
        }

        return dataList.stream()
                .filter(item -> item instanceof Map)
                .map(item -> (Map<?, ?>) item)
                .collect(Collectors.toMap(
                        tier -> (String) tier.get("uuid"),
                        tier -> (String) tier.get("devName"),
                        (existing, replacement) -> existing
                ));
    }

    private Integer getRandomPrice() {
        int[] prices = {0, 475, 875, 1275, 1775, 2175, 2475, 3550, 4350};
        return prices[random.nextInt(prices.length)];
    }

    private String getRandomSource() {
        String[] sources = {"BOUGHT", "BATTLEPASS", "AGENT"};
        int roll = random.nextInt(100);
        if (roll < 70) return "BOUGHT";
        if (roll < 90) return "BATTLEPASS";
        return "AGENT";
    }
}