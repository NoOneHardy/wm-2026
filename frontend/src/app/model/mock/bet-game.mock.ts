import {BetGame} from '../game/bet-game'

export const mockBetGame1: BetGame = Object.freeze({
  id: 'game-1',
  timestamp: new Date('2026-06-11T18:00:00'),
  teamHome: {
    id: 'team-1',
    name: 'Schweiz',
    shortName: 'SUI',
    flag: '/assets/user.jpg',
    previousGames: []
  },
  teamGuest: {
    id: 'team-2',
    name: 'Deutschland',
    shortName: 'GER',
    flag: '/assets/user.jpg',
    previousGames: []
  },
  result: null,
  bet: null,
  groupId: 'group-1',
  groupName: 'Gruppe A'
})

export const mockBetGame2: BetGame = Object.freeze({
  ...mockBetGame1,
  id: 'game-2',
  timestamp: new Date('2026-06-12T21:00:00'),
  bet: {
    id: 'bet-1',
    gameId: 'game-2',
    scoreTeamHome: 2,
    scoreTeamGuest: 1,
    joker: 1 as const
  }
})

export const mockBetGame3: BetGame = Object.freeze({
  ...mockBetGame1,
  id: 'game-3',
  timestamp: new Date('2026-06-10T15:00:00'),
  result: {
    id: 'result-1',
    gameId: 'game-3',
    scoreTeamHome: 3,
    scoreTeamGuest: 0
  },
  bet: {
    id: 'bet-2',
    gameId: 'game-3',
    scoreTeamHome: 3,
    scoreTeamGuest: 0,
    joker: 2 as const
  }
})
