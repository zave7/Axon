package com.cqrs.command.commands

import com.cqrs.command.dto.TransferDTO
import com.cqrs.common.command.transfer.factory.TransferCommandFactory
import com.cqrs.common.command.transfer.JejuBankTransferCommand
import com.cqrs.common.command.transfer.JejuBankCompensationCancelCommand
import com.cqrs.common.command.transfer.JejuBankCancelTransferCommand
import com.cqrs.common.command.transfer.SeoulBankTransferCommand
import com.cqrs.common.command.transfer.SeoulBankCompensationCancelCommand
import com.cqrs.common.command.transfer.SeoulBankCancelTransferCommand
import java.util.UUID
import java.util.function.Function
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class MoneyTransferCommand(
    val srcAccountId: String,
    @TargetAggregateIdentifier
    val dstAccountId: String,
    val amount: Long,
    val transferId: String,
    val bankType: BankType
) {

    /**
     * 계좌이체 요청시, 은행 구분에 따른 Command 생성을 달리하기 위해서 Enum 사용
     * 또한, DTO 클래스를 Command 클래스로 변환하기 위한 Factory 메서드 추가
     */
    enum class BankType(val expression: Function<MoneyTransferCommand, TransferCommandFactory>) {

        JEJU({ TransferCommandFactory(JejuBankTransferCommand(), JejuBankCancelTransferCommand(), JejuBankCompensationCancelCommand()) }),
        SEOUL({ TransferCommandFactory(SeoulBankTransferCommand(), SeoulBankCancelTransferCommand(), SeoulBankCompensationCancelCommand()) });

        fun getCommandFactory(command: MoneyTransferCommand) : TransferCommandFactory {
            return this.expression.apply(command).apply {
                create(
                    srcAccountId = command.srcAccountId
                    , dstAccountId = command.dstAccountId
                    , amount = command.amount
                    , transferId = command.transferId
                )
            }
        }

    }

    companion object {
        fun of(dto: TransferDTO) : MoneyTransferCommand {
            return MoneyTransferCommand(
                srcAccountId = dto.srcAccountId
                , dstAccountId = dto.dstAccountId
                , amount = dto.amount
                , bankType = dto.bankType
                , transferId = UUID.randomUUID().toString()
            )
        }
    }
}