package org.gjdd.batoru.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.gjdd.batoru.job.Job;
import org.gjdd.batoru.registry.BatoruRegistries;

import java.util.Collection;
import java.util.List;

public final class JobCommand {
    private static final DynamicCommandExceptionType UNKNOWN_JOB_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("job.notFound", id)
    );
    private static final DynamicCommandExceptionType FAILED_ENTITY_EXCEPTION = new DynamicCommandExceptionType(
            entityName -> Text.translatable("commands.job.failed.entity", entityName)
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("job")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.literal("get")
                                        .executes(context ->
                                                executeGet(
                                                        context.getSource(),
                                                        getLivingEntity(context.getSource().getEntityOrThrow())
                                                )
                                        ).then(
                                                CommandManager.argument("target", EntityArgumentType.entity())
                                                        .executes(context ->
                                                                executeGet(
                                                                        context.getSource(),
                                                                        getLivingEntity(EntityArgumentType.getEntity(context, "target"))
                                                                )
                                                        )
                                        )
                        ).then(
                                CommandManager.literal("set")
                                        .then(
                                                CommandManager.argument("job", IdentifierArgumentType.identifier())
                                                        .suggests((context, builder) -> CommandSource.suggestIdentifiers(BatoruRegistries.JOB.getIds(), builder))
                                                        .executes(context ->
                                                                executeSet(
                                                                        context.getSource(),
                                                                        getJob(IdentifierArgumentType.getIdentifier(context, "job")),
                                                                        List.of(context.getSource().getEntityOrThrow())
                                                                )
                                                        ).then(
                                                                CommandManager.argument("targets", EntityArgumentType.entities())
                                                                        .executes(context ->
                                                                                executeSet(
                                                                                        context.getSource(),
                                                                                        getJob(IdentifierArgumentType.getIdentifier(context, "job")),
                                                                                        EntityArgumentType.getEntities(context, "targets")
                                                                                )
                                                                        )
                                                        )
                                        )
                        ).then(
                                CommandManager.literal("clear")
                                        .executes(context ->
                                                executeClear(
                                                        context.getSource(),
                                                        List.of(context.getSource().getEntityOrThrow())
                                                )
                                        ).then(
                                                CommandManager.argument("targets", EntityArgumentType.entities())
                                                        .executes(context ->
                                                                executeClear(
                                                                        context.getSource(),
                                                                        EntityArgumentType.getEntities(context, "targets")
                                                                )
                                                        )
                                        )
                        )
        );
    }

    private static int executeGet(ServerCommandSource source, LivingEntity target) {
        var job = target.getJob();
        if (job == null) {
            source.sendFeedback(() -> Text.translatable("commands.job.get.empty", target.getName()), false);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.job.get.success", target.getName(), job.value().getName()), false);
        }

        return 1;
    }

    private static int executeSet(ServerCommandSource source, RegistryEntry<Job> job, Collection<? extends Entity> targets) throws CommandSyntaxException {
        for (var target : targets) {
            getLivingEntity(target).setJob(job);
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.job.set.success.single", targets.iterator().next().getName(), job.value().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.job.set.success.multiple", targets.size(), job.value().getName()), true);
        }

        return targets.size();
    }

    private static int executeClear(ServerCommandSource source, Collection<? extends Entity> targets) throws CommandSyntaxException {
        for (var target : targets) {
            getLivingEntity(target).setJob(null);
        }

        if (targets.size() == 1) {
            source.sendFeedback(() -> Text.translatable("commands.job.clear.success.single", targets.iterator().next().getName()), true);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.job.clear.success.multiple", targets.size()), true);
        }

        return targets.size();
    }

    private static LivingEntity getLivingEntity(Entity entity) throws CommandSyntaxException {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        } else {
            throw FAILED_ENTITY_EXCEPTION.create(entity.getName());
        }
    }

    private static RegistryEntry<Job> getJob(Identifier id) throws CommandSyntaxException {
        return BatoruRegistries.JOB
                .getEntry(id)
                .orElseThrow(() -> UNKNOWN_JOB_EXCEPTION.create(id));
    }
}
