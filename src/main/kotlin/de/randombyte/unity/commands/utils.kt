package de.randombyte.unity.commands

import de.randombyte.kosp.extensions.toText
import de.randombyte.unity.config.Config
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import java.util.*

fun Player.checkRequester(requestee: Player, requests: Map<UUID, List<UUID>>): Boolean {
    if (this == requestee) return false
    val requestsToPlayer = requests[requestee.uniqueId] ?: emptyList()
    return this.uniqueId in requestsToPlayer
}

fun List<Config.Unity>.getUnities(playerUuid: UUID): List<Config.Unity>{
    val returned = mutableListOf<Config.Unity>()
    for(unity in this.listIterator()){
        if(unity.member1 == playerUuid || unity.member2 == playerUuid) returned.add(unity)
    }
    return returned.toList()
}

fun List<Config.Unity>.findUnity(playerUuid: UUID, targetUuid: UUID): Config.Unity? {
    for(unity in this.getUnities(playerUuid).listIterator()){
        if(unity.getOtherMember(playerUuid).equals(targetUuid)) return unity
    }
    return null
}

fun broadcastIfNotEmpty(text: Text) {
    if (!text.isEmpty) Sponge.getServer().broadcastChannel.send(text)
}

fun Player.sendMessageIfNotEmpty(text: Text) {
    if (!text.isEmpty) this.sendMessage(text)
}