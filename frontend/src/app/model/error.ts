export interface ServiceError {
  error: {
    status: number
    message: string
    timestamp: Date,
    displayMessage: string
  },
  status: number
}
