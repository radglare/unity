package de.randombyte.unity.commands

import de.randombyte.kosp.extensions.getPlayer
import de.randombyte.kosp.extensions.getUser
import de.randombyte.kosp.extensions.toText
import de.randombyte.unity.Unity
import de.randombyte.unity.config.Config
import de.randombyte.unity.config.ConfigAccessor
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player

class TeleportCommand(
        configAccessor: ConfigAccessor
) : UnityCommand(configAccessor) {
    override fun executedByUnityMember(player: Player, args: CommandContext, unityList: List<Config.Unity>, config: Config): CommandResult {
        val target = args.getOne<Player>(Unity.PLAYER_ARG).get()
        val thisUnity = unityList.findUnity(player.uniqueId, target.uniqueId) ?: throw CommandException("You are not married to that person!".toText())
        val otherMember = thisUnity.getOtherMember(player.uniqueId)
        val otherPlayer = otherMember.getPlayer() ?: throw CommandException("Partner must be online to execute this command!".toText())
        player.location = otherPlayer.location

        return CommandResult.success()
    }
}