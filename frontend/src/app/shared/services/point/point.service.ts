import {Injectable} from '@angular/core'
import {BetGame} from '../../../model/game/bet-game'
import {DetailedPoints} from '../../../model/game/detailed-points'
import {Bet} from '../../../model/game/bet'
import {Score} from '../../../model/game/score'

@Injectable({
  providedIn: 'root'
})
export class PointService {
  calculateDetailedPoints(game: BetGame): DetailedPoints | null {
    if (!game.bet || !game.result) return null

    const detailedPoints: DetailedPoints = {}

    const correctWinnerHome = this.isHomeTeamWinner(game.bet) && this.isHomeTeamWinner(game.result)
    const correctWinnerGuest = this.isGuestTeamWinner(game.bet) && this.isGuestTeamWinner(game.result)
    const correctWinnerTie = this.isTie(game.bet) && this.isTie(game.result)

    if (correctWinnerHome || correctWinnerGuest || correctWinnerTie) {
      detailedPoints.correctWinner = 50
    }

    if (game.bet.scoreTeamGuest === game.result.scoreTeamGuest) {
      detailedPoints.correctGoalsGuest = 20
    }
    if (game.bet.scoreTeamHome === game.result.scoreTeamHome) {
      detailedPoints.correctGoalsHome = 20
    }
    if (game.bet.scoreTeamGuest + game.bet.scoreTeamHome === game.result.scoreTeamGuest + game.result.scoreTeamHome) {
      detailedPoints.correctGoalsTotal = 10
    }

    if (game.bet.joker !== 1) detailedPoints.joker = game.bet.joker

    return detailedPoints
  }

  private isHomeTeamWinner(score: Bet | Score): boolean {
    return score.scoreTeamHome > score.scoreTeamGuest
  }

  private isGuestTeamWinner(score: Bet | Score): boolean {
    return score.scoreTeamGuest > score.scoreTeamHome
  }

  private isTie(score: Bet | Score): boolean {
    return score.scoreTeamHome === score.scoreTeamGuest
  }

  getTotal(detailedPoints: DetailedPoints): number {
    let total = 0

    if (detailedPoints.correctWinner) total += detailedPoints.correctWinner
    if (detailedPoints.correctGoalsHome) total += detailedPoints.correctGoalsHome
    if (detailedPoints.correctGoalsGuest) total += detailedPoints.correctGoalsGuest
    if (detailedPoints.correctGoalsTotal) total += detailedPoints.correctGoalsTotal
    if (detailedPoints.joker) total *= detailedPoints.joker

    return total
  }

  calculatePoints(game: BetGame): number {
    const detailedPoints = this.calculateDetailedPoints(game)
    if (!detailedPoints) return 0

    return this.getTotal(detailedPoints)
  }
}
