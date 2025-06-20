export interface ServiceError {
  error: {
    status: number
    message: string
    timestamp: Date
  },
  status: number
}
