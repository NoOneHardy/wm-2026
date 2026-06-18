import {ShortNumberPipe} from './short-number.pipe'

describe('ShortNumberPipe', () => {
  let pipe: ShortNumberPipe

  beforeEach(() => {
    pipe = new ShortNumberPipe()
  })

  it('should create an instance', () => {
    expect(pipe).toBeTruthy()
  })

  describe('values below 1,000', () => {
    it('should display 0 as-is', () => {
      expect(pipe.transform(0)).toBe('0')
    })

    it('should display a single-digit value as-is', () => {
      expect(pipe.transform(7)).toBe('7')
    })

    it('should display a two-digit value as-is', () => {
      expect(pipe.transform(42)).toBe('42')
    })

    it('should display a three-digit value as-is', () => {
      expect(pipe.transform(500)).toBe('500')
    })

    it('should display 999 as-is (upper boundary)', () => {
      expect(pipe.transform(999)).toBe('999')
    })
  })

  describe('values of 1,000 or more', () => {
    it('should display 1000 as 1.00k', () => {
      expect(pipe.transform(1000)).toBe('1.00k')
    })

    it('should display 1234 as 1.23k', () => {
      expect(pipe.transform(1234)).toBe('1.23k')
    })

    it('should display 12345 as 12.3k', () => {
      expect(pipe.transform(12345)).toBe('12.3k')
    })

    it('should display 123456 as 123k', () => {
      expect(pipe.transform(123456)).toBe('123k')
    })
  })

  describe('exactly 3 significant digits', () => {
    it('should keep 3 significant digits for a value rounding down', () => {
      expect(pipe.transform(1004)).toBe('1.00k')
    })

    it('should keep 3 significant digits for a value rounding up', () => {
      expect(pipe.transform(1996)).toBe('2.00k')
    })

    it('should keep 3 significant digits in the tens-of-thousands range', () => {
      expect(pipe.transform(45678)).toBe('45.7k')
    })

    it('should keep 3 significant digits in the hundreds-of-thousands range', () => {
      expect(pipe.transform(999000)).toBe('999k')
    })
  })
})
