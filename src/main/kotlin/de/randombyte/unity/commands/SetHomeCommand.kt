package de.randombyte.unity.commands

import de.randombyte.kosp.extensions.green
import de.randombyte.kosp.extensions.toText
import de.randombyte.unity.Unity
import de.randombyte.unity.config.Config
import de.randombyte.unity.config.Config.Unity.ConfigLocation
import de.randombyte.unity.config.ConfigAccessor
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User

class SetHomeCommand(
        configAccessor: ConfigAccessor
) : UnityCommand(configAccessor) {
    override fun executedByUnityMember(player: Player, args: CommandContext, unityList: List<Config.Unity>, config: Config): CommandResult {
        val target = args.getOne<User>(Unity.USER_ARG).get()
        val thisUnity = unityList.findUnity(player.uniqueId, target.uniqueId) ?: throw CommandException("You are not married to that person!".toText())
        val newHome = player.location
        configAccessor.set(config.copy(
                unities = (config.unities - thisUnity) + thisUnity.copy(home = ConfigLocation(newHome))))

        thisUnity.sendMessage("New home set".green())

        return CommandResult.success()
    }
}