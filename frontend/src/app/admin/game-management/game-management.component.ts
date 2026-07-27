import {Component, computed, DestroyRef, ElementRef, inject, OnInit, Signal, signal, viewChild} from '@angular/core'
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormGroupDirective,
  NgForm,
  ReactiveFormsModule,
  ValidatorFn,
  Validators
} from '@angular/forms'
import {ButtonComponent} from '../../shared/components/button/button.component'
import {NgOptimizedImage} from '@angular/common'
import {AdminService} from '../admin.service'
import {finalize, forkJoin} from 'rxjs'
import {takeUntilDestroyed} from '@angular/core/rxjs-interop'
import {GroupOption} from '../model/group-option'
import {LightTeam} from '../../model/team/light-team'
import {hasError} from '../../shared/helper/form-field-error'
import {BetGame} from '../../model/game/bet-game'
import {SnackbarService} from '../../shared/services/snackbar/snackbar.service'
import {SpinnerComponent} from '../../shared/components/spinner/spinner.component'
import {MatFormFieldModule} from '@angular/material/form-field'
import {MatInput} from '@angular/material/input'
import {MatDatepickerModule} from '@angular/material/datepicker'
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from '@angular/material/autocomplete'
import {MatIcon} from '@angular/material/icon'
import {MatTimepicker, MatTimepickerInput, MatTimepickerToggle} from '@angular/material/timepicker'
import {ErrorStateMatcher} from '@angular/material/core'

@Component({
  selector: 'bet-game-management',
  imports: [
    ReactiveFormsModule,
    ButtonComponent,
    NgOptimizedImage,
    SpinnerComponent,
    MatFormFieldModule,
    MatDatepickerModule,
    MatInput,
    MatOption,
    MatAutocompleteTrigger,
    MatAutocomplete,
    MatIcon,
    MatTimepickerInput,
    MatTimepickerToggle,
    MatTimepicker
  ],
  templateUrl: './game-management.component.html',
  styleUrl: './game-management.component.css'
})
export class GameManagementComponent implements OnInit {
  private adminService = inject(AdminService)
  private snackbarService = inject(SnackbarService)
  private destroyRef = inject(DestroyRef)

  isLoadingOptions = signal(true)
  isSaving = signal(false)
  groups = signal<GroupOption[]>([])
  teams = signal<LightTeam[]>([])
  createdGame = signal<BetGame | null>(null)
  groupSearch = signal('')
  teamHomeSearch = signal('')
  teamGuestSearch = signal('')

  private groupSearchInput: Signal<ElementRef<HTMLInputElement> | undefined> = viewChild('groupSearchInput')
  private teamHomeSearchInput: Signal<ElementRef<HTMLInputElement> | undefined> = viewChild('teamHomeSearchInput')
  private teamGuestSearchInput: Signal<ElementRef<HTMLInputElement> | undefined> = viewChild('teamGuestSearchInput')

  filteredGroups = computed(() => this.filterOptions(this.groups(), this.groupSearch(), (group) => this.groupDisplayFn(group)))
  filteredHomeTeams = computed(() => this.filterOptions(this.teams(), this.teamHomeSearch(), team => this.teamDisplayFn(team)))
  filteredGuestTeams = computed(() => this.filterOptions(this.teams(), this.teamGuestSearch(), team => this.teamDisplayFn(team)))

  formGroup = new FormGroup({
    date: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    time: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    group: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    teamHome: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    teamGuest: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    })
  }, {
    validators: [this.differentTeamsValidator()]
  })

  ngOnInit(): void {
    forkJoin({
      groups: this.adminService.getGroupOptions(),
      teams: this.adminService.getTeams()
    }).pipe(
      finalize(() => this.isLoadingOptions.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(({groups, teams}) => {
      this.groups.set([...groups].sort((a, b) => a.order - b.order || a.name.localeCompare(b.name)))
      this.teams.set([...teams].sort((a, b) => a.name.localeCompare(b.name)))
      this.formGroup.controls.group.updateValueAndValidity({emitEvent: false})
      this.formGroup.controls.teamHome.updateValueAndValidity({emitEvent: false})
      this.formGroup.controls.teamGuest.updateValueAndValidity({emitEvent: false})
    })
  }

  submit(): void {
    this.formGroup.markAllAsTouched()
    if (this.formGroup.invalid) return

    const value = this.formGroup.getRawValue()
    const group = this.findGroupById(value.group)
    const teamHome = this.findTeamById(value.teamHome)
    const teamGuest = this.findTeamById(value.teamGuest)
    if (!group || !teamHome || !teamGuest) return

    this.isSaving.set(true)
    this.adminService.createGame({
      timestamp: this.buildTimestamp(new Date(value.date), new Date(value.time)),
      group: group.id,
      teamHome: teamHome.id,
      teamGuest: teamGuest.id
    }).pipe(
      finalize(() => this.isSaving.set(false)),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(game => {
      this.createdGame.set(game)
      this.snackbarService.addMessage({
        type: 'success',
        message: `${teamHome.name} gegen ${teamGuest.name} wurde angelegt`
      })
      this.formGroup.reset()
      this.resetGroupSearch()
      this.resetHomeTeamSearch()
      this.resetGuestTeamSearch()
    })
  }

  groupDisplayFn(groupOrId?: GroupOption | string | null): string {
    const group = typeof groupOrId === 'string' ? this.findGroupById(groupOrId) : groupOrId
    if (!group) return ''
    return group.name
  }

  teamDisplayFn(teamOrId?: LightTeam | string | null): string {
    const team = typeof teamOrId === 'string' ? this.findTeamById(teamOrId) : teamOrId
    if (!team) return ''
    return `${team.name} (${team.shortName})`
  }

  get selectedGroup(): GroupOption | null {
    return this.findGroupById(this.formGroup.controls.group.value)
  }

  get selectedHomeTeam(): LightTeam | null {
    return this.findTeamById(this.formGroup.controls.teamHome.value)
  }

  get selectedGuestTeam(): LightTeam | null {
    return this.findTeamById(this.formGroup.controls.teamGuest.value)
  }

  filterGroups(): void {
    const value = this.groupSearchInput()?.nativeElement.value ?? ''
    this.groupSearch.set(value)
  }

  filterHomeTeams(): void {
    const value = this.teamHomeSearchInput()?.nativeElement.value ?? ''
    this.teamHomeSearch.set(value)
  }

  filterGuestTeams(): void {
    const value = this.teamGuestSearchInput()?.nativeElement.value ?? ''
    this.teamGuestSearch.set(value)
  }

  findGroupById(id: string): GroupOption | null {
    return this.groups().find(g => g.id === id) ?? null
  }

  findTeamById(id: string): LightTeam | null {
    return this.teams().find(t => t.id === id) ?? null
  }

  resetGroupSearch(): void {
    this.groupSearch.set('')
  }

  resetHomeTeamSearch(): void {
    this.teamHomeSearch.set('')
  }

  resetGuestTeamSearch(): void {
    this.teamGuestSearch.set('')
  }

  guestTeamErrorStateMatcher: ErrorStateMatcher = {
    isErrorState(control: AbstractControl | null, form: FormGroupDirective | NgForm | null): boolean {
      return !!control && (hasError(control) || form?.hasError('sameTeams') || false)
    }
  }

  private filterOptions<T>(options: T[], query: string, getLabel: (option: T) => string): T[] {
    const normalizedQuery = query.trim().toLowerCase()
    if (!normalizedQuery) return options

    return options.filter(option => getLabel(option).toLowerCase().includes(normalizedQuery))
  }

  private differentTeamsValidator(): ValidatorFn {
    return (group: AbstractControl) => {
      const teamHome = `${group.get('teamHome')?.value ?? ''}`.trim()
      const teamGuest = `${group.get('teamGuest')?.value ?? ''}`.trim()
      if (!teamHome || !teamGuest) return null
      return teamHome === teamGuest ? {sameTeams: true} : null
    }
  }

  private buildTimestamp(date: Date, time: Date): string {
    date.setHours(time.getHours() + 2, time.getMinutes())
    return date.toISOString()
  }

  protected readonly hasError = hasError
}
