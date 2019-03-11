package de.randombyte.unity.commands

import de.randombyte.kosp.extensions.toText
import de.randombyte.unity.Unity
import de.randombyte.unity.config.Config
import de.randombyte.unity.config.Config.Unity.HomeLocation.*
import de.randombyte.unity.config.Config.Unity.HomeLocation.Set
import de.randombyte.unity.config.ConfigAccessor
import org.spongepowered.api.command.CommandException
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User

class HomeCommand(
        configAccessor: ConfigAccessor
) : UnityCommand(configAccessor) {
    override fun executedByUnityMember(player: Player, args: CommandContext, unityList: List<Config.Unity>, config: Config): CommandResult {
        val target = args.getOne<User>(Unity.USER_ARG).get()
        val thisUnity = unityList.findUnity(player.uniqueId, target.uniqueId) ?: throw CommandException("You are not married to that person!".toText())
        val home = thisUnity.tryGetHomeLocation()
        when (home) {
            NotSet -> throw CommandException("No home set!".toText())
            Unreachable -> throw CommandException("The home is not reachable! The world is not loaded.".toText())
            is Set -> player.location = home.location
        }

        return CommandResult.success()
    }
}