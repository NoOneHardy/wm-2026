import {TestBed} from '@angular/core/testing'

import {PointService} from './point.service'
import {BetGame} from '../../../model/game/bet-game'
import {DetailedPoints} from '../../../model/game/detailed-points'

describe('PointService', () => {
  let service: PointService
  let mockGame: BetGame

  beforeEach(() => {
    TestBed.configureTestingModule({})
    service = TestBed.inject(PointService)

    mockGame = {
      id: '',
      teamGuest: {
        id: '',
        flag: '',
        name: '',
        shortName: '',
        previousGames: []
      },
      teamHome: {
        id: '',
        flag: '',
        name: '',
        shortName: '',
        previousGames: []
      },
      timestamp: new Date(),
      bet: null,
      result: null,
      groupId: '',
      groupName: ''
    }
  })

  it('should be created', () => {
    expect(service).toBeTruthy()
  })

  const checkPoints = (_game: BetGame, expectedDetails: DetailedPoints, expectedPoints: number): void => {
    const game: BetGame = {
      ..._game,
      bet: _game.bet ? {..._game.bet} : null,
      result: _game.result ? {..._game.result} : null
    }
    if (!game.bet || !game.result) return

    let points = service.calculateDetailedPoints(game)
    expect(points).toEqual(expectedDetails)

    game.bet.joker = 1
    points = service.calculateDetailedPoints(game)
    expect(service.calculatePoints(game)).toEqual(expectedPoints)
    if (points) expect(service.getTotal(points)).toEqual(expectedPoints)

    game.bet.joker = 2
    points = service.calculateDetailedPoints(game)
    expect(service.calculatePoints(game)).toEqual(expectedPoints * 2)
    if (points) expect(service.getTotal(points)).toEqual(expectedPoints * 2)

    game.bet.joker = 3
    points = service.calculateDetailedPoints(game)
    if (points) expect(service.getTotal(points)).toEqual(expectedPoints * 3)
  }

  it('should calculate points for correct bet', () => {
    mockGame.bet = {
      scoreTeamGuest: 2,
      scoreTeamHome: 1,
      joker: 1,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 2,
      scoreTeamHome: 1,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      correctWinner: 5,
      correctGoalsHome: 2,
      correctGoalsGuest: 2,
      correctGoalsTotal: 1
    }, 10)

    mockGame.bet.scoreTeamGuest = 1
    mockGame.bet.scoreTeamHome = 2
    mockGame.result.scoreTeamGuest = 1
    mockGame.result.scoreTeamHome = 2
    checkPoints(mockGame, {
      correctWinner: 5,
      correctGoalsHome: 2,
      correctGoalsGuest: 2,
      correctGoalsTotal: 1
    }, 10)

    mockGame.bet.scoreTeamGuest = 0
    mockGame.bet.scoreTeamHome = 0
    mockGame.result.scoreTeamGuest = 0
    mockGame.result.scoreTeamHome = 0
    checkPoints(mockGame, {
      correctWinner: 5,
      correctGoalsHome: 2,
      correctGoalsGuest: 2,
      correctGoalsTotal: 1
    }, 10)
  })

  it('should calculate points for correct winner with correct goals of one team', () => {
    mockGame.bet = {
      scoreTeamGuest: 1,
      scoreTeamHome: 2,
      joker: 1,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 0,
      scoreTeamHome: 2,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      correctWinner: 5,
      correctGoalsHome: 2,
    }, 7)

    mockGame.bet.scoreTeamGuest = 2
    mockGame.bet.scoreTeamHome = 1
    mockGame.result.scoreTeamGuest = 2
    mockGame.result.scoreTeamHome = 0
    checkPoints(mockGame, {
      correctWinner: 5,
      correctGoalsGuest: 2,
    }, 7)
  })

  it('should calculate points for tie with incorrect goals', () => {
    mockGame.bet = {
      scoreTeamGuest: 2,
      scoreTeamHome: 2,
      joker: 1,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 1,
      scoreTeamHome: 1,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      correctWinner: 5
    }, 5)
  })

  it('should calculate points for correct winner with correct total goals', () => {
    mockGame.bet = {
      scoreTeamGuest: 1,
      scoreTeamHome: 2,
      joker: 1,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 0,
      scoreTeamHome: 3,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      correctWinner: 5,
      correctGoalsTotal: 1,
    }, 6)

    mockGame.bet.scoreTeamGuest = 2
    mockGame.bet.scoreTeamHome = 1
    mockGame.result.scoreTeamGuest = 3
    mockGame.result.scoreTeamHome = 0
    checkPoints(mockGame, {
      correctWinner: 5,
      correctGoalsTotal: 1
    }, 6)
  })

  it('should calculate points for correct winner without any bonus', () => {
    mockGame.bet = {
      scoreTeamGuest: 1,
      scoreTeamHome: 2,
      joker: 1,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 0,
      scoreTeamHome: 4,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      correctWinner: 5
    }, 5)

    mockGame.bet.scoreTeamGuest = 2
    mockGame.bet.scoreTeamHome = 1
    mockGame.result.scoreTeamGuest = 4
    mockGame.result.scoreTeamHome = 2

    checkPoints(mockGame, {
      correctWinner: 5
    }, 5)
  })

  it('should calculate points for correct goals of one team', () => {
    mockGame.bet = {
      scoreTeamGuest: 1,
      scoreTeamHome: 2,
      joker: 1,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 1,
      scoreTeamHome: 0,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      correctGoalsGuest: 2
    }, 2)

    mockGame.bet.scoreTeamGuest = 2
    mockGame.bet.scoreTeamHome = 1
    mockGame.result.scoreTeamGuest = 0
    mockGame.result.scoreTeamHome = 1
    checkPoints(mockGame, {
      correctGoalsHome: 2
    }, 2)
  })

  it('should calculate points for correct total goals', () => {
    mockGame.bet = {
      scoreTeamGuest: 1,
      scoreTeamHome: 2,
      joker: 1,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 3,
      scoreTeamHome: 0,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      correctGoalsTotal: 1,
    }, 1)

    mockGame.bet.scoreTeamGuest = 4
    mockGame.bet.scoreTeamHome = 1
    mockGame.result.scoreTeamGuest = 2
    mockGame.result.scoreTeamHome = 3

    checkPoints(mockGame, {
      correctGoalsTotal: 1
    }, 1)
  })

  it('should calculate points for no bonus', () => {
    mockGame.bet = {
      scoreTeamGuest: 1,
      scoreTeamHome: 2,
      joker: 2,
      id: '',
      gameId: ''
    }

    mockGame.result = {
      scoreTeamGuest: 0,
      scoreTeamHome: 0,
      gameId: '',
      id: ''
    }

    checkPoints(mockGame, {
      joker: 2
    }, 0)

    mockGame.bet = null
    mockGame.result = null
    checkPoints(mockGame, {}, 0)
  })
})
